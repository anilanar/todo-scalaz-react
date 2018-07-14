package tutorial.webapp.services

import scalaz._
import scalaz.Scalaz._
import scalaz.effect._
import tutorial.webapp.algebra._
import tutorial.webapp.model.{Incomplete, TodoItem}
//import tutorial.webapp.model._

object TodoStorage {
  final case class StorageState(
    todos: ImmutableArray[TodoItem] = ImmutableArray.fromArray(Array.empty),
    nextId: Int = 0
  )
  final class TodoStorageBusiness[F[_]: Monad](implicit
    P: PersistentStorage[F],
    M: MemoryStorage[F]
  )
  {
    def addTodo(s: String): F[Unit] = for {
      nextId <- P.nextId
      newItem = Incomplete(s, nextId)
      _ <- P.add(newItem)
      _ <- M.add(newItem)
    } yield ()

    def removeTodo(t: TodoItem): F[Unit] = for {
      _ <- P.remove(t)
      _ <- M.remove(t)
    } yield ()
  }
  object TodoStorageBusiness {
    def apply[F[_]](implicit
      M: Monad[F],
      PS: PersistentStorage[F],
      MS: MemoryStorage[F]
    ) = new TodoStorageBusiness[F]
  }

  implicit def memoryStorageStateT[M[_]: Monad]: MemoryStorage[StateT[M, StorageState, ?]] = new MemoryStorage[StateT[M, StorageState, ?]] {
    type ST[T] = StateT[M, StorageState, T]
    def add(item: TodoItem): ST[Unit] = for {
      state <- StateT.get[M, StorageState]
      _ <- StateT.put[M, StorageState](state.copy(
        state.todos ++ ImmutableArray.fromArray(Array(item)),
        item.id + 1
      ))
    } yield ()
    def remove(item: TodoItem): ST[Unit] =
      StateT.modify[M, StorageState](state =>
        state.copy(state.todos filter (_.id != item.id))
      )
    def getAll: ST[ImmutableArray[TodoItem]] = StateT.get[M, StorageState].map(_.todos)
  }

  implicit val persistentStorageIO = new PersistentStorage[IO] {
    val nextIdRef = IORef.ioRef(STRef[IvoryTower](1))
    def add(item: TodoItem) = nextIdRef.mod(_ + 1).map(_ => ())
    def remove(item: TodoItem) = IO.ioMonad.point(())
    def nextId = nextIdRef.read
    def getAll =
      IO.ioMonad.point(ImmutableArray.fromArray(Array.empty[TodoItem]))
  }

  implicit val persistentStorageStateTIO = new PersistentStorage[StateT[IO, StorageState, ?]] {
    type ST[T] = StateT[IO, StorageState, T]
    private val PIO = implicitly[PersistentStorage[IO]]

    def add(item: TodoItem) = PIO.add(item).liftIO[ST]
    def remove(item: TodoItem) = PIO.remove(item).liftIO[ST]
    def nextId = PIO.nextId.liftIO[ST]
    def getAll = PIO.getAll.liftIO[ST]
  }
}

