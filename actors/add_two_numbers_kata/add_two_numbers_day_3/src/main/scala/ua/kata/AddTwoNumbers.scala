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
    case ComputeNumbersWithOverflow(Nil, Nil, overflow) => sender() ! NumbersComputed(overflow.filter(item => item != 0))

    case ComputeNumbersWithOverflow(first, Nil, overflow) => computeWithSingleNum(sender(), first, overflow)

    case ComputeNumbersWithOverflow(Nil, second, overflow) => computeWithSingleNum(sender(), second, overflow)

    case ComputeNumbersWithOverflow(first, second, overflow) =>
      val requester: ActorRef = sender()
      val num: Int = (first.head :: second.head :: overflow).sum
      (self ? ComputeNumbersWithOverflow(first.tail, second.tail, List(num / 10)))
        .mapTo[NumbersComputed]
        .andThen { case Success(NumbersComputed(digits)) => requester ! NumbersComputed((num % 10) :: digits) }

    case ComputeNumbers(first, second) =>
      val requester: ActorRef = sender()
      (self ? ComputeNumbersWithOverflow(first, second, List.empty))
        .mapTo[NumbersComputed]
        .andThen { case Success(numbers) => requester ! numbers }
  }

  private def computeWithSingleNum(requester: ActorRef, num: List[Int], overflow: List[Int]): Unit = {
    overflow match {
      case Nil => requester ! NumbersComputed(num)
      case digit :: Nil => (self ? ComputeNumbersWithOverflow(num.tail, Nil, List((digit + num.head) / 10)))
        .mapTo[NumbersComputed]
        .andThen { case Success(NumbersComputed(digits)) => requester ! NumbersComputed(((digit + num.head) % 10) :: digits) }
    }
  }

  private case class ComputeNumbersWithOverflow(first: List[Int], second: List[Int], overflow: List[Int])

}
