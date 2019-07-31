package ua.kata

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

class AddTwoNumbers extends Actor {
  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  override def receive: Receive = {
    case ComputeNumbers(first, second) =>
      computeDigitRecursively(first, second, List.empty, list => NumbersComputed(list))

    case ComputeNumbersWithOverflow(Nil, Nil, overflow) =>
      sender() ! overflow.flatMap {
        case 0 => List.empty
        case digit => List(digit)
      }

    case ComputeNumbersWithOverflow(first, Nil, overflow) =>
      sender() ! (overflow.head + first.head) :: first.tail

    case ComputeNumbersWithOverflow(Nil, second, overflow) =>
      sender() ! (overflow.head + second.head) :: second.tail

    case ComputeNumbersWithOverflow(first, second, overflow) =>
      computeDigitRecursively(first, second, overflow, list => list)
  }


  private def computeDigitRecursively[M](first: List[Int], second: List[Int], overflow: List[Int], createResponse: List[Int] => M): Unit = {
    val requester: ActorRef = sender()
    val item: Int = (first.head :: second.head :: overflow).sum
    (self ? ComputeNumbersWithOverflow(first.tail, second.tail, List(item / 10)))
      .mapTo[List[Int]]
      .onComplete { case Success(ret) => requester ! createResponse((item % 10) :: ret) }
  }

  private[AddTwoNumbers] case class ComputeNumbersWithOverflow(first: List[Int], second: List[Int], overflow: List[Int])

}
