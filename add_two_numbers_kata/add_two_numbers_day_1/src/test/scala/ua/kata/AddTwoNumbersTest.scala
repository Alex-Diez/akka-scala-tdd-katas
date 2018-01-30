package ua.kata

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpecLike}

class AddTwoNumbersTest(testSystem: ActorSystem)
  extends TestKit(testSystem)
  with WordSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  def this() = this(ActorSystem("AddTwoNumbersTestSystem"))

  override def afterAll(): Unit = TestKit.shutdownActorSystem(testSystem)

  private var addTwoNumbers: ActorRef = _

  override def beforeEach(): Unit = addTwoNumbers = testSystem.actorOf(Props[AddTwoNumbers])

  private def from(num: Int): List[Int] = {
    if (num > 9) {
      (num % 10) :: from(num / 10)
    } else {
      List(num)
    }
  }

  "Add Two Number system" must {
    "compute sum of two zeros" in {
      addTwoNumbers ! ComputeNumbers(List(0), List(0))

      expectMsg(NumbersComputed(List(0)))
    }

    "compute single digit numbers" in {
      addTwoNumbers ! ComputeNumbers(List(1), List(2))

      expectMsg(NumbersComputed(List(3)))
    }

    "compute single digit numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(List(7), List(6))

      expectMsg(NumbersComputed(from(13)))
    }

    "compute two digits numbers without overflow" in {
      addTwoNumbers ! ComputeNumbers(from(23), from(34))

      expectMsg(NumbersComputed(from(57)))
    }

    "compute two digits numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(from(19), from(13))

      expectMsg(NumbersComputed(from(32)))
    }

    "compute three digits numbers without overflow" in {
      addTwoNumbers ! ComputeNumbers(from(999), from(2))

      expectMsg(NumbersComputed(from(1001)))
    }
  }
}
