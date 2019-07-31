package ua.kata

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.SECONDS
import scala.util.Success

class AddTwoNumbers extends Actor {
  private implicit val timeout: Timeout = new Timeout(5, SECONDS)

  override def receive: Receive = {
    case ComputeNumbersWithOverflow(Nil, Nil, overflow) =>
      sender() ! NumbersComputed(List(overflow).filter(item => item != 0))

    case ComputeNumbersWithOverflow(first, second, overflow) =>
      val requester: ActorRef = sender()
      val num: Int = first.headOption.getOrElse(0) + second.headOption.getOrElse(0) + overflow
       (self ? ComputeNumbersWithOverflow(first.drop(1), second.drop(1), num / 10))
         .mapTo[NumbersComputed]
         .andThen{case Success(NumbersComputed(digits)) => requester ! NumbersComputed((num % 10) :: digits) }

    case ComputeNumbers(first, second) =>
      val requester: ActorRef = sender()
      (self ? ComputeNumbersWithOverflow(first, second, 0))
        .mapTo[NumbersComputed]
        .andThen{case Success(NumbersComputed(result)) => requester ! NumbersComputed(result) }
  }

  private case class ComputeNumbersWithOverflow(first: List[Int], second: List[Int], overflow: Int)
}
