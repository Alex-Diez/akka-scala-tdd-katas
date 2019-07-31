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

  "Add Two Numbers System" must {
    "add two zeros" in {
      addTwoNumbers ! ComputeNumbers(List(0), List(0))

      expectMsg(NumbersComputed(List(0)))
    }

    "add single digit numbers" in {
      addTwoNumbers ! ComputeNumbers(List(3), List(4))

      expectMsg(NumbersComputed(List(7)))
    }

    "add single digit numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(List(7), List(8))

      expectMsg(NumbersComputed(from(15)))
    }

    "add two digits numbers" in {
      addTwoNumbers ! ComputeNumbers(from(11), from(22))

      expectMsg(NumbersComputed(from(33)))
    }

    "add two digits numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(from(90), from(10))

      expectMsg(NumbersComputed(from(100)))
    }

    "add two digits numbers with overflow from first digit" in {
      addTwoNumbers ! ComputeNumbers(from(99), from(2))

      expectMsg(NumbersComputed(from(101)))

      addTwoNumbers ! ComputeNumbers(from(2), from(99))

      expectMsg(NumbersComputed(from(101)))
    }
  }
}
