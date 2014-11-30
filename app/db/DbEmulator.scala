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
  private val InitialDelay = 100
  private val MaxRandomDelay = 200

  private val storage = new AtomicReference[List[T]](defaultItems)

  def list: Future[List[T]] = futureResult {
    storage.get
  }

  def insert(entity: T): Future[Option[T]] = futureResult {
    storage.getAndSet(entity :: storage.get())
    Some(entity)
  }

  def update(entity: T): Future[Option[T]] = futureResult {
    storage.getAndSet(entity :: storage.get().filterNot(_.id == entity.id))
    Some(entity)
  }

  def delete(entityId: String): Future[Boolean] = futureResult {
    storage.getAndSet(storage.get().filterNot(_.id == entityId))
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