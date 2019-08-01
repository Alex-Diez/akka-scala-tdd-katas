package ua.kata

import akka.NotUsed
import akka.stream.scaladsl.Source

sealed trait Tree

case class Node(elem: Int) extends Tree

case object Empty extends Tree {

}

class PathSum {
  def compute(tree: Tree, target: Int): Source[List[Int], NotUsed] = tree match {
    case Node(elem) if elem == target => Source(List(List(elem)))
    case _ => Source(List())
  }
}
