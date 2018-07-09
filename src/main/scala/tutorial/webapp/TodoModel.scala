package tutorial.webapp.model

sealed abstract class TodoItem {
  def title: String
  def id: Int
}
final case class Incomplete(title: String, id: Int) extends TodoItem
final case class Complete(title: String, id: Int) extends TodoItem
