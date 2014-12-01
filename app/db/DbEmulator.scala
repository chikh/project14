package db

import java.util.concurrent.atomic.AtomicReference

import actors.Contexts._
import models.{Product, User, WithId}

import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.util.Random


object DbEmulator {
  private val nameToCollection = Map[String, DbEmulatorCollection[_ <: WithId]](
    classOf[Product].getName -> new DbEmulatorCollection[models.Product](DefaultValues.DefaultProducts),
    classOf[User].getName -> new DbEmulatorCollection[User](DefaultValues.DefaultUsers)
  )

  def collection[T <: WithId : ClassTag]: DbEmulatorCollection[T] =
    nameToCollection(scala.reflect.classTag[T].runtimeClass.getName).asInstanceOf[DbEmulatorCollection[T]] //todo: get rid of "asInstanceOf"
}

class DbEmulatorCollection[T <: WithId](defaultItems: List[T] = Nil) {
  private val InitialDelay = 150
  private val MaxRandomDelay = 100

  implicit private def toRichAtomicReference[V](atomicReference: AtomicReference[V]) = new {

    /**
     * Mostly copy-pasted from [[java.util.concurrent.atomic.AtomicReference]] getAndSet method.
     * @param newValueFromOld - function: oldValue => newValue
     */
    def compareAndSetFun(newValueFromOld: V => V): Unit = {
      while (true) {
        val oldValue = atomicReference.get
        if (atomicReference.compareAndSet(oldValue, newValueFromOld(oldValue)))
          return
      }
    }
  }

  private val storage = new AtomicReference[List[T]](defaultItems)

  def list: Future[List[T]] = futureResult {
    storage.get
  }

  def insert(entity: T): Future[Option[T]] = futureResult {
    storage.compareAndSetFun(entity :: _)
    Some(entity)
  }

  def update(entity: T): Future[Option[T]] = futureResult {
    storage.compareAndSetFun(entity :: _.filterNot(_.id == entity.id))
    Some(entity)
  }

  def update(entities: List[T]): Future[Option[List[T]]] = futureResult {
    storage.compareAndSetFun(entities ++ _.filterNot(e => entities.exists(_.id == e.id))) //todo: reduce complexity
    Some(entities)
  }

  def delete(entityId: String): Future[Boolean] = futureResult {
    storage.compareAndSetFun(_.filterNot(_.id == entityId))
    true
  }

  def find(entityId: String): Future[Option[T]] = futureResult {
    storage.get().find(_.id == entityId)
  }

  private def futureResult[R](fun: => R) = Future {
    threadSleep()
    fun
  }

  private def threadSleep() {
    Thread.sleep(randomLatency)
  }

  private def randomLatency: Long = {
    InitialDelay + Random.nextInt(MaxRandomDelay)
  }
}

object DefaultValues {
  val DefaultProducts = List(
    Product("Гвозди", 1.5, 130),
    Product("Коты", 777.7, 5),
    Product("Печеньки", 23.3, 23),
    Product("Что-то там еще", 1, 10)
  )
  val DefaultUsers = List(
    User("admin", "admin")
  )
}