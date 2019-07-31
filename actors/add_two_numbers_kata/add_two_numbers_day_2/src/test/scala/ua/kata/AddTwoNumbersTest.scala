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
    num match {
      case 0 => List.empty
      case _ => (num % 10) :: from(num / 10)
    }
  }

  "Add Two Numbers System" must {
    "add two zeros" in {
      addTwoNumbers ! ComputeNumbers(List(0), List(0))

      expectMsg(NumbersComputed(List(0)))
    }

    "add two single digit numbers" in {
      addTwoNumbers ! ComputeNumbers(List(4), List(5))

      expectMsg(NumbersComputed(List(9)))
    }

    "add two single digit numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(List(8), List(5))

      expectMsg(NumbersComputed(from(13)))
    }

    "add two digits numbers" in {
      addTwoNumbers ! ComputeNumbers(from(11), from(22))

      expectMsg(NumbersComputed(from(33)))
    }

    "add two digits numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(from(99), from(11))

      expectMsg(NumbersComputed(from(110)))
    }

    "add three digits numbers" in {
      addTwoNumbers ! ComputeNumbers(from(111), from(222))

      expectMsg(NumbersComputed(from(333)))
    }

    "add three digits numbers with overflow" in {
      addTwoNumbers ! ComputeNumbers(from(111), from(999))

      expectMsg(NumbersComputed(from(1110)))
    }

    "add long and short numbers" in {
      addTwoNumbers ! ComputeNumbers(from(10009), from(1))

      expectMsg(NumbersComputed(from(10010)))

      addTwoNumbers ! ComputeNumbers(from(1), from(10009))

      expectMsg(NumbersComputed(from(10010)))
    }
  }
}
