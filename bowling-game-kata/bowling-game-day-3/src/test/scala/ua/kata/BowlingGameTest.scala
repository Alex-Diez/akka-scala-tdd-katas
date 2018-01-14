package ua.kata

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpecLike}

class BowlingGameTest(testSystem: ActorSystem)
  extends TestKit(testSystem)
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  def this() = this(ActorSystem("BowlingGameTestSystem"))

  override def afterAll(): Unit = TestKit.shutdownActorSystem(testSystem)

  private var game: ActorRef = _

  override def beforeEach(): Unit = game = testSystem.actorOf(Props[BowlingGame])

  private def rollMany(times: Int, pin: Int): Unit = {
    for (_ <- 1 to times) {
      game ! Roll(pin)
    }
  }

  private def rollSpare(): Unit = {
    game ! Roll(4)
    game ! Roll(6)
  }

  private def rollStrike(): Unit = {
    game ! Roll(10)
  }

  "Bowling actor system" must {
    "process gutter game" in {
      rollMany(20, 0)

      game ! ComputeScore()

      expectMsg(ScoreComputed(0))
    }

    "process all ones" in {
      rollMany(20, 1)

      game ! ComputeScore()

      expectMsg(ScoreComputed(20))
    }

    "process one spare" in {
      rollSpare()
      game ! Roll(3)
      rollMany(17, 0)

      game ! ComputeScore()

      expectMsg(ScoreComputed(16))
    }

    "process one strike" in {
      rollStrike()
      game ! Roll(4)
      game ! Roll(3)
      rollMany(16, 0)

      game ! ComputeScore()

      expectMsg(ScoreComputed(24))
    }

    "process perfect game" in {
      rollMany(12, 10)

      game ! ComputeScore()

      expectMsg(ScoreComputed(300))
    }
  }
}
