package ua.kata

import akka.actor.Actor

class BowlingGame extends Actor {
  private var rolls: List[Int] = List.empty

  override def receive: Receive = {
    case Roll(pin) => rolls = pin :: rolls
    case ComputeScore() =>
      val player = sender()
      player ! ScoreComputed(score(0, 1, rolls.reverse))
  }

  private def score(currentScore: Int, frameIndex: Int, rolls: List[Int]): Int = {
    def computeScore(numberOfRolls: Int, rest: List[Int]): Int =
      score(currentScore + rolls.take(numberOfRolls).sum, frameIndex + 1, rest)

    def isSpare: Boolean = rolls.take(2).sum == 10

    def isStrike: Boolean = rolls.head == 10

    (frameIndex, rolls) match {
      case (11, _) => currentScore
      case (_, _ :: other) if isStrike => computeScore(3, other)
      case (_, _ :: _ :: other) if isSpare => computeScore(3, other)
      case (_, _ :: _ :: other) => computeScore(2, other)
    }
  }
}
