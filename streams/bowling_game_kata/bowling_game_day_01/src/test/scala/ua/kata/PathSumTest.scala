package ua.kata

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.stream.testkit.TestSubscriber
import akka.stream.testkit.scaladsl.TestSink
import org.scalatest.{BeforeAndAfterEach, FunSuite}

class PathSumTest extends FunSuite with BeforeAndAfterEach {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private var pathSum: PathSum = _
  private val sink: Sink[List[Int], TestSubscriber.Probe[List[Int]]] = TestSink.probe[List[Int]]

  override def beforeEach {
    pathSum = new PathSum()
  }

  test("sum is zero when tree is empty") {
    pathSum
      .compute(Empty, 10)
      .runWith(sink)
      .request(1)
      .expectComplete()
  }

  test("only root and sum equals the root element") {
    pathSum
      .compute(Node(10), 10)
      .runWith(sink)
      .request(1)
      .expectNext(List(10))
      .expectComplete()
  }

  test("only root sum not equals the root element") {
    pathSum
      .compute(Node(5), 10)
      .runWith(sink)
      .request(1)
      .expectComplete()
  }
}
