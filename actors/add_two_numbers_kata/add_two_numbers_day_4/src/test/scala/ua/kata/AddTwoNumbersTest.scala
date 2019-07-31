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

  private def from(num: Int): List[Int] = num match {
    case 0 => Nil
    case _ => (num % 10) :: from(num / 10)
  }

  "Add Two Numbers system" must {
    "add two zeros" in {
      addTwoNumbers ! ComputeNumbers(List(0), List(0))

      expectMsg(NumbersComputed(List(0)))
    }

    "add single digit numbers" in {
      addTwoNumbers ! ComputeNumbers(List(1), List(2))

      expectMsg(NumbersComputed(List(3)))
    }

    "add single digit numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(List(8), List(9))

      expectMsg(NumbersComputed(from(17)))
    }

    "add two digits numbers" in {
      addTwoNumbers ! ComputeNumbers(from(11), from(22))

      expectMsg(NumbersComputed(from(33)))
    }

    "add two digits numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(from(90), from(10))

      expectMsg(NumbersComputed(from(100)))

      addTwoNumbers ! ComputeNumbers(from(99), from(1))

      expectMsg(NumbersComputed(from(100)))

      addTwoNumbers ! ComputeNumbers(from(1), from(99))

      expectMsg(NumbersComputed(from(100)))
    }
  }
}
