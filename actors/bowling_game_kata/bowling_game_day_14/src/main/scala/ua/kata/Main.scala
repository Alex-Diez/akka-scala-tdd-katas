package ua.kata

import java.nio.file.Paths
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import akka.{Done, NotUsed}

import scala.concurrent.{ExecutionContextExecutor, Future}

object Main extends App {
  def lineSink(filename: String): Sink[String, Future[IOResult]] = {
    val f: Flow[String, ByteString, NotUsed] =
      Flow[String].map(s => ByteString(s + "\n"))

    f.toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
  }

  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val indexes: Source[Int, NotUsed] = Source(1 to 100)

  val factorials = indexes.scan(BigInt(1))((acc, next) => acc * next)

  val done: Future[Done] = factorials
    .zipWith(indexes)((num, index) => s"$index != $num")
    .throttle(1, 1.second)
    .runForeach(i => println(i))

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  done.onComplete(_ => system.terminate())
}
