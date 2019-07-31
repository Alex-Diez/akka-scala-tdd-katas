package ua.kata

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.SECONDS
import scala.util.Success

class AddTwoNumbers extends Actor {
  private implicit val timeout: Timeout = Timeout(5, SECONDS)

  override def receive: Receive = {
    case ComputeNumbersWithOverflow(Nil, Nil, overflow) => sender() ! overflow.filter(item => item != 0)

    case ComputeNumbersWithOverflow(first, second, overflow) =>
      val num: Int = first.headOption.getOrElse(0) + second.headOption.getOrElse(0) + overflow.head
      val requester: ActorRef = sender()
      (self ? ComputeNumbersWithOverflow(first.drop(1), second.drop(1), List(num / 10)))
        .mapTo[List[Int]]
        .onComplete { case Success(digits) => requester ! (num % 10) :: digits }

    case ComputeNumbers(first, second) =>
      val requester: ActorRef = sender()
      (self ? ComputeNumbersWithOverflow(first, second, List(0)))
        .mapTo[List[Int]]
        .onComplete { case Success(digits) => requester ! NumbersComputed(digits) }
  }

  private case class ComputeNumbersWithOverflow(first: List[Int], second: List[Int], overflow: List[Int])

}
