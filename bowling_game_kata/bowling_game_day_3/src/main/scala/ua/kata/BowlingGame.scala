package ua.kata

import akka.actor.Actor

class BowlingGame extends Actor {
  private var rolls: List[Int] = List.empty

  override def receive: Receive = {
    case Roll(pin) => rolls = pin :: rolls
    case ComputeScore() =>
      val player = sender()
      player ! ScoreComputed(computeScore(0, 1, rolls.reverse))
  }

  private def computeScore(score: Int, frameIndex: Int, rolls: List[Int]): Int = {
    def compute(numberOfRolls: Int, rest: List[Int]): Int =
      computeScore(score + rolls.take(numberOfRolls).sum, frameIndex + 1, rest)

    def isSpare: Boolean = rolls.take(2).sum == 10

    if (frameIndex > 10) score
    else rolls match {
      case 10 :: rest => compute(strikeRoll + strikeBonusRolls, rest)
      case _ :: _ :: rest if isSpare => compute(frameRolls + spareBonusRolls, rest)
      case _ :: _ :: rest => compute(frameRolls, rest)
    }
  }

  private def strikeRoll: Int = 1

  private def strikeBonusRolls: Int = 2

  private def spareBonusRolls: Int = 1

  private def frameRolls: Int = 2
}
