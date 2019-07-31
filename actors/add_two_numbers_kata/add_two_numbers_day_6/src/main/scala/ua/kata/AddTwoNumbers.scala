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
    case ComputeNumbersWithOverflow(Nil, Nil, overflow) =>
      sender() ! NumbersComputed(overflow.filter(item => item != 0))

    case ComputeNumbersWithOverflow(first, second, overflow) =>
      val num: Int = first.headOption.getOrElse(0) + second.headOption.getOrElse(0) + overflow.headOption.getOrElse(0)
      computeAsync(
        first.drop(1), second.drop(1), List(num / 10), sender(), digits => NumbersComputed((num % 10) :: digits)
      )

    case ComputeNumbers(first, second) => computeAsync(first, second, Nil, sender(), digits => NumbersComputed(digits))
  }

  private def computeAsync(
      first:        List[Int],
      second:       List[Int],
      overflow:     List[Int],
      requester:    ActorRef,
      makeResponse: List[Int] => NumbersComputed): Unit = {

    (self ? ComputeNumbersWithOverflow(first, second, overflow))
      .mapTo[NumbersComputed]
      .onComplete { case Success(NumbersComputed(digit)) => requester ! makeResponse(digit) }
  }

  private case class ComputeNumbersWithOverflow(first: List[Int], second: List[Int], overflow: List[Int])

}
