package ua.kata

import akka.NotUsed
import akka.stream.scaladsl.Source

trait Tree

case object Empty extends Tree

case class Node(item: Int, left: Tree = Empty, right: Tree = Empty) extends Tree

class PathSum {
  def compute(root: Tree, target: Int): Source[List[Int], NotUsed] = root match {
    case Node(_, _, _) => Source(compute(root, List(), List(), target).reverse)
    case Empty => Source.empty
  }

  private def compute(tree: Tree, path: List[Int], paths: List[List[Int]], target: Int): List[List[Int]] = tree match {
    case Node(item, Empty, Empty) if (item :: path).sum == target => (item :: path).reverse :: paths
    case Node(item, Empty, Empty) if (item :: path).sum != target => paths
    case Node(item, left, right) => {
      val p = compute(left, item :: path, paths, target)
      compute(right, item :: path, p, target)
    }
    case Empty => paths
  }
}
