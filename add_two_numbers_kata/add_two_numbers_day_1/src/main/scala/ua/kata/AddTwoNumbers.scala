package ua.kata

import akka.actor.Actor

class AddTwoNumbers extends Actor {
  override def receive: Receive = {
    case ComputeNumbers(first, second) => sender() ! NumbersComputed(compute(first, second))
  }

  private def compute(first: List[Int], second: List[Int]): List[Int] = {
    (first, second) match {
      case (firstHead :: firstTail, secondHead :: secondTail) =>
        val num: Int = firstHead + secondHead
        if (num > 9) {
          num % 10 :: compute(firstTail, compute(List(num / 10), secondTail))
        } else {
          num :: compute(firstTail, secondTail)
        }
      case (Nil, secondList) => secondList
      case (firstList, Nil) => firstList
    }
  }
}
