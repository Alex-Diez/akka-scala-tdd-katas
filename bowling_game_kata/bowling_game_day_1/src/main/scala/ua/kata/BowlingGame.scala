package ua.kata

import akka.actor.Actor

import scala.collection.mutable.ArrayBuffer

class BowlingGame extends Actor {
  private var rolls: ArrayBuffer[Int] = new ArrayBuffer[Int](21)

  override def receive: Receive = {
    case Roll(pin) => rolls += pin
    case ComputeScore =>
      val player = sender
      val score = frameIndexes().take(10)
        .map(frameIndex => {
          if (isStrike(frameIndex)) 10 + strikeBonus(frameIndex)
          else if (isSpare(frameIndex)) 10 + spareBonus(frameIndex)
          else rolls(frameIndex) + rolls(frameIndex + 1)
        }).sum
      player ! ScoreComputed(score)
  }

  private def frameIndexes(): Stream[Int] = {
    def nextFrameIndex(frameIndex: Int): Stream[Int] = {
      if (isStrike(frameIndex)) frameIndex #:: nextFrameIndex(frameIndex + 1)
      else frameIndex #:: nextFrameIndex(frameIndex + 2)
    }

    nextFrameIndex(0)
  }

  private def isStrike(frameIndex: Int) = rolls(frameIndex) == 10

  private def strikeBonus(frameIndex: Int): Int = rolls(frameIndex + 1) + rolls(frameIndex + 2)

  private def isSpare(frameIndex: Int) = rolls(frameIndex) + rolls(frameIndex + 1) == 10

  private def spareBonus(frameIndex: Int) = rolls(frameIndex + 2)
}
