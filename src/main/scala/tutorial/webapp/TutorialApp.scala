package tutorial.webapp

import scalaz._
import japgolly.scalajs.react._
import vdom.html_<^._
import ScalazReact._
import org.scalajs.dom.document
import scalaz.effect.IO
import tutorial.webapp.model._
import tutorial.webapp.services.TodoStorage.{StorageState, TodoStorageBusiness}

final object TutorialApp {
  def run(args: ImmutableArray[String]): IO[Unit] = {
    val todos: ImmutableArray[TodoItem] = ImmutableArray.fromArray(Array.empty)
    val comp = AppComponent.C()
    IO { comp.renderIntoDOM(document.body) }.map(_ => ())
  }

  def main(args: Array[String]): Unit = {
    run(ImmutableArray.fromArray(args)).unsafePerformIO
  }
}


final object AppComponent {
//  val ST = ReactS.Fix[State]
//  type Foo[M[_], A] = StateT[M, State, A]
  private val TS = TodoStorageBusiness[StateT[IO, StorageState, ?]]

  final class Backend($: BackendScope[Unit, StorageState]) {
    def addItem(t: String) = $.runState(TS.addTodo(t).liftS)
    def removeItem(item: TodoItem) = $.runState(TS.removeTodo(item).liftS)
    def render(state: StorageState) =
      ReactFragment(
        <.p("Todo App"),
        InputComponent.C(
          InputComponent.Props(addItem _)
        ),
        TodoListComponent(
          TodoListComponent.Props(state.todos, removeItem _)
        )
      )
  }

  val C = ScalaComponent.builder[Unit]("AppComponent")
    .initialState(StorageState())
    .renderBackend[Backend]
    .build
}

final object TodoItemComponent {
  final case class Props(item: TodoItem, removeItem: TodoItem => CallbackTo[Unit])
  final class Backend($: BackendScope[Props, Unit]) {
    def removeItem(props: Props) = props.removeItem(props.item)
    def render(props: Props) =
      ReactFragment(
        <.span(
          props.item.title
        ),
        <.button(
          ^.onClick --> removeItem(props),
          ^.title := "Remove",
          "X"
        )
      )

  }
  val C = ScalaComponent.builder[Props]("TodoItemComponent")
    .renderBackend[Backend]
    .build
}

final object TodoListComponent {
  final case class Props(todos: ImmutableArray[TodoItem], removeItem: TodoItem => CallbackTo[Unit])
  val C = ScalaComponent.builder[Props]("TodoListComponent")
    .renderP(($, props) =>
      <.ul(
        props.todos.toSeq.toVdomArray(todoItem =>
          <.li(
            ^.key := todoItem.id,
            TodoItemComponent.C(TodoItemComponent.Props(todoItem, props.removeItem))
          )
        )
      )
    )
    .build
  def apply(props: Props) = C(props)
}

final object InputComponent {
  type State = String
  val ST = ReactS.Fix[State]

  final case class Props(acceptInput: String => CallbackTo[Unit])
  final class Backend($: BackendScope[Props, State]) {
    def acceptChange(e: ReactEventFromInput) = ST.set(e.target.value)
    def acceptInput(submit: String => CallbackTo[Unit]): CallbackTo[Unit] =
      $.runState(
        for {
          title <- ST.get
          _ <- ST.set("")
        } yield title
      ) >>= submit
    def render(props: Props, state: State) =
      ReactFragment(
        <.input.text(
          ^.value := state,
          ^.onChange ==> $.runStateFn(acceptChange)
        ),
        <.button(
          ^.onClick --> acceptInput(props.acceptInput),
          ^.title := "Add",
          "Add"
        )
      )
  }

  val C = ScalaComponent.builder[Props]("InputComponent")
    .initialState("")
    .renderBackend[Backend]
    .build
}
