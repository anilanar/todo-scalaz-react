package tutorial.webapp.algebra

import scalaz.{Foldable, ImmutableArray}
import tutorial.webapp.model.{Incomplete, TodoItem}

trait Storage[F[_]] {
  def add(item: TodoItem): F[Unit]
  def remove(item: TodoItem): F[Unit]
  def getAll: F[ImmutableArray[TodoItem]]
}
trait PersistentStorage[F[_]] extends Storage[F] {
  def nextId: F[Int]
}
trait MemoryStorage[F[_]] extends Storage[F]

object Storage { self =>
  def add[F[_]](item: TodoItem)(implicit S: Storage[F]): F[Unit] = S.add(item)
  def remove[F[_]](item: TodoItem)(implicit S: Storage[F]): F[Unit] = S.remove(item)
  def getAll[F[_]](implicit S: Storage[F]): F[ImmutableArray[TodoItem]] = S.getAll
}

object PersistentStorage {
  def add[F[_]](item: TodoItem)(implicit S: PersistentStorage[F]): F[Unit] = S.add(item)
  def remove[F[_]](item: TodoItem)(implicit S: PersistentStorage[F]): F[Unit] = S.remove(item)
  def getAll[F[_]](implicit S: PersistentStorage[F]): F[ImmutableArray[TodoItem]] = S.getAll
  def nextId[F[_]](implicit S: PersistentStorage[F]): F[Int] = S.nextId
}

object MemoryStorage {
  def add[F[_]](item: TodoItem)(implicit S: MemoryStorage[F]): F[Unit] = S.add(item)
  def remove[F[_]](item: TodoItem)(implicit S: MemoryStorage[F]): F[Unit] = S.remove(item)
  def getAll[F[_]](implicit S: MemoryStorage[F]): F[ImmutableArray[TodoItem]] = S.getAll
}

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
//}



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


