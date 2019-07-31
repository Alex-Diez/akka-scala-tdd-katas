package ua.kata

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.SECONDS
import scala.util.Success

class AddTwoNumbers extends Actor {
  private implicit val timeout: Timeout = Timeout(1, SECONDS)

  type Num = List[Int]

  override def receive: Receive = {
    case ComputeNumbersWithOverflow(Nil, Nil, overflow) => sender() ! NumbersComputed(overflow.filter(it => it != 0))

    case ComputeNumbersWithOverflow(first, second, overflow) =>
      val num: Int = first.headOption.getOrElse(0) + second.headOption.getOrElse(0) + overflow.headOption.getOrElse(0)
      computeAsync(first.drop(1), second.drop(1), List(num / 10), sender(), digits => NumbersComputed((num % 10) :: digits))

    case ComputeNumbers(first, second) =>
      computeAsync(first, second, Nil, sender(), digit => NumbersComputed(digit))
  }

  private def computeAsync(first: Num, second: Num, overflow: Num, requester: ActorRef, toResponse: Num => NumbersComputed): Unit = {
    (self ? ComputeNumbersWithOverflow(first, second, overflow))
      .mapTo[NumbersComputed]
      .onComplete {case Success(NumbersComputed(digits)) => requester ! toResponse(digits)}
  }

  private case class ComputeNumbersWithOverflow(first: Num, second: Num, overflow: Num)
}
