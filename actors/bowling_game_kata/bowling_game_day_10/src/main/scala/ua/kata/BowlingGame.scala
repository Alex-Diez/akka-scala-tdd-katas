package ua.kata

import akka.actor.Actor

class BowlingGame extends Actor {
  private var pins: Int = 0
  private var rolls: List[Int] = List.empty

  override def receive: Receive = {
    case Roll(pin) => rolls = pin :: rolls
    case ComputeScore() =>
      val player = sender()
      player ! ScoreComputed(score(0, 1, rolls.reverse))
  }

  def score(currentScore: Int, frameIndex: Int, rolls: List[Int]): Int = {
    def isStrike: Boolean = rolls.head == 10

    def strikeBonus(): Int = rolls.tail.take(2).sum

    def isSpare: Boolean = framePoints() == 10

    def spareBonus(): Int = rolls.drop(2).head

    def framePoints(): Int = rolls.take(2).sum

    (frameIndex, rolls) match {
      case (11, _) => currentScore
      case (_, _ :: rest) if isStrike => score(10 + strikeBonus() + currentScore, frameIndex + 1, rest)
      case (_, _ :: _ :: rest) if isSpare => score(10 + spareBonus() + currentScore, frameIndex + 1, rest)
      case (_, _ :: _ :: rest) => score(framePoints() + currentScore, frameIndex + 1, rest)
    }
  }
}
