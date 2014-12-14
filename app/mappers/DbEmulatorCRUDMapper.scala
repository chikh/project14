package mappers

import db.DbEmulator._
import models.WithId

import scala.concurrent.Future

class DbEmulatorCRUDMapper extends CRUDController {
  def list[T <: WithId] = collection[T].list

  def update[T <: WithId](selector: (List[T]) => Boolean, entitiesUpdateFun: (List[T]) => List[T]) =
    collection[T].update(entitiesUpdateFun, selector)

  def delete[T <: WithId](id: String): Future[Boolean] = collection[T].delete(id)

  def read[T <: WithId](id: String): Future[Option[T]] = collection[T].find(id)

  def create[T <: WithId](entity: T): Future[Option[T]] = collection[T].insert(entity)
}
