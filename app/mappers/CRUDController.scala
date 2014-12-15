package mappers

import models.WithId

import scala.concurrent.Future
import scala.reflect.ClassTag

trait CRUDController {
  def list[T <: WithId : ClassTag]: Future[List[T]]
  def create[T <: WithId : ClassTag](entity: T): Future[Option[T]]
  def read[T <: WithId : ClassTag](id: String): Future[Option[T]]
  def update[T <: WithId : ClassTag](selector: List[T] => Boolean, entitiesUpdateFun: List[T] => List[T]): Future[Boolean]
  def delete[T <: WithId : ClassTag](entityId: String): Future[Boolean]
}