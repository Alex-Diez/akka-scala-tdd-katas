package ua.kata

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.testkit.scaladsl.TestSink
import org.scalatest.FunSuite

class PathSumTest extends FunSuite {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  test("empty paths when empty tree") {
    new PathSum()
      .compute(Empty, 10)
      .runWith(TestSink.probe[List[Int]])
      .request(1)
      .expectComplete
  }

  test("single item when tree has only root and it equals target") {
    new PathSum()
      .compute(Node(10), 10)
      .runWith(TestSink.probe[List[Int]])
      .request(1)
      .expectNext(List(10))
      .expectComplete
  }

  test("empty paths when root item is not equal to target") {
    new PathSum()
      .compute(Node(5), 10)
      .runWith(TestSink.probe[List[Int]])
      .request(1)
      .expectComplete
  }

  test("tree with left leaf") {
    new PathSum()
      .compute(Node(10, Node(20)), 30)
      .runWith(TestSink.probe[List[Int]])
      .request(2)
      .expectNext(List(10, 20))
      .expectComplete()
  }

  test("tree with left and right leaves") {
    new PathSum()
      .compute(Node(10, Node(20), Node(20)), 30)
      .runWith(TestSink.probe[List[Int]])
      .request(2)
      .expectNext(List(10, 20))
      .expectNext(List(10, 20))
      .expectComplete()
  }
}
