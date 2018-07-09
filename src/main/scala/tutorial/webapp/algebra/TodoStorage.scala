package tutorial.webapp.algebra

import japgolly.scalajs.react.ScalazReact._
import japgolly.scalajs.react._
import scalaz.Scalaz._
import scalaz._
import scalaz.effect._
import tutorial.webapp.model.{Incomplete, TodoItem}


object TodoStorage {
  sealed abstract case class TodoState(todos: ImmutableArray[TodoItem])

  trait Storage[F[_], T] {
    def add(item: TodoItem): F[Unit]
    def remove(item: TodoItem): F[Unit]
    def nextId: F[Int]
  }
  trait PersistentStorage[F[_], T] extends Storage[F, T]
  trait MemoryStorage[F[_], T] extends Storage[F, T]

//  implicit val todoPersistantStorage = new PersistentStorage[IO, TodoItem] {
//    def add(item: TodoItem) =
//    def remove(item: TodoItem) = IO.ioUnit
//    val nextId = IO.ioMonad.pure(0)
//  }
//  final case class TodoBusiness[F[_]: Monad, T](implicit
//                                                S: PersistentStorage[F, T],
//                                                ToCallback: F ~> CallbackTo
//                                               )
//  {
//    def add(item: TodoItem): CallbackTo[Unit] = ToCallback(for {
//      state <- S.nextId
//      _ <- S.add(item)
//    } yield ())
//    //    state <- MS.get
//    //    nextId <- S.nextId
//    //    newItem = Incomplete(state.todos, nextId)
//    //    _ <- MS.put(state.copy(
//    //      state.todos ++ ImmutableArray.fromArray(Array(newItem)),
//    //      nextId
//    //    ))
//    //  } yield ()
//  }
//
//  def foo = TodoBusiness[IO, TodoItem].add(Incomplete("", 0))
}



//final object TodoStorageIdentity {
//  implicit val todoStorageIdentity = new TodoStorage[Id.Id] {
//    def add(item: TodoItem) = ()
//    def remove(item: TodoItem) = ()
//  }
//}
//
//
//final object TodoStorageCallback {
//  implicit val todoStorageCallback = new TodoStorage[CallbackTo] {
//    def add(item: TodoItem) = CallbackTo.pure(())
//    def remove(item: TodoItem) = CallbackTo.pure(())
//  }
//}


