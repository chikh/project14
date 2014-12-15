package mappers

import db.DbEmulator._
import models.WithId

import scala.reflect.ClassTag

class DbEmulatorCRUDMapper extends CRUDController {
  def list[T <: WithId : ClassTag] = collection[T].list

  def update[T <: WithId : ClassTag](selector: (List[T]) => Boolean, entitiesUpdateFun: (List[T]) => List[T]) =
    collection[T].update(entitiesUpdateFun, selector)

  def delete[T <: WithId : ClassTag](id: String) = collection[T].delete(id)

  def read[T <: WithId : ClassTag](id: String) = collection[T].find(id)

  def create[T <: WithId : ClassTag](entity: T) = collection[T].insert(entity)
}
