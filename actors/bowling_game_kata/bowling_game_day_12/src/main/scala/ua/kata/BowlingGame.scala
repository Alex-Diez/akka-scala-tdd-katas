package ua.kata

import akka.actor.Actor

class BowlingGame extends Actor {
  private var rolls: List[Int] = List.empty

  override def receive: Receive = {
    case Roll(pin) => rolls = pin :: rolls
    case ComputeScore() => sender() ! ScoreComputed(score(0, 1, rolls.reverse))
  }

  private def score(currentScore: Int, frameIndex: Int, rolls: List[Int]): Int = {
    def framePoints: Int = rolls.take(2).sum

    def isSpare = framePoints == 10

    def spareBonus: Int = rolls.drop(2).head

    def strikeBonus: Int = rolls.slice(1, 3).sum

    def isStrike: Boolean = rolls.head == 10

    (frameIndex, rolls) match {
      case (11, _) => currentScore
      case (_, _ :: rest) if isStrike => score(10 + strikeBonus + currentScore, frameIndex + 1, rest)
      case (_, _ :: _ :: rest) if isSpare => score(10 + spareBonus + currentScore, frameIndex + 1, rest)
      case (_, _ :: _ :: rest) => score(framePoints + currentScore, frameIndex + 1, rest)
    }
  }
}
