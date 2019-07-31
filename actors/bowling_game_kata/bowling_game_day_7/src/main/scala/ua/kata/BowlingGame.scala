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
    def isSpare: Boolean = rolls.take(2).sum == 10

    def spareBonus: Int = rolls.slice(2, 3).sum

    def isStrike: Boolean = rolls.head == 10

    def strikeBonus: Int = rolls.slice(1, 3).sum

    (frameIndex, rolls) match {
      case (11, _) => currentScore
      case (_, _ :: rest) if isStrike => score(10 + strikeBonus + currentScore, frameIndex + 1, rest)
      case (_, _ :: _ :: rest) if isSpare => score(10 + spareBonus + currentScore, frameIndex + 1, rest)
      case (_, _ :: _ :: rest) => score(rolls.take(2).sum + currentScore, frameIndex + 1, rest)
    }
  }
}
