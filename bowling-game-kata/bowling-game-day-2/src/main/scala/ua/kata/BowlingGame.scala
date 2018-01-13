package ua.kata

import akka.actor.Actor

class BowlingGame extends Actor {
  private var rolls: List[Int] = List.empty

  override def receive: Receive = {
    case Roll(pin) => rolls = pin :: rolls
    case ComputeScore() =>
      val player = sender()
      val pins = computeScore()
      player ! ScoreComputed(pins)
  }

  def computeScore(): Int = {
    def framePoints(rolls: List[Int]): Int = {
      rolls match {
        case rollOne :: rollTwo :: Nil => rollOne + rollTwo
        case rollOne :: rollTwo :: rollThree :: Nil => rollOne + rollTwo + rollThree
        case rollOne :: rollTwo :: rollThree :: rest if isStrike(rollOne) => 10 + rollTwo + rollThree + framePoints(rollTwo :: rollThree :: rest)
        case rollOne :: rollTwo :: rollThree :: rest if isSpare(rollOne, rollTwo) => 10 + rollThree + framePoints(rollThree :: rest)
        case rollOne :: rollTwo :: rollThree :: rest => rollOne + rollTwo + framePoints(rollThree :: rest)
      }
    }

    framePoints(rolls.reverse)
  }

  private def isSpare(rollOne: Int, rollTwo: Int): Boolean = rollOne + rollTwo == 10

  private def isStrike(rollOne: Int): Boolean = rollOne == 10
}
