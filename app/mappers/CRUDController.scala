package mappers

import scala.concurrent.Future

trait CRUDController {
  def list[T]: Future[List[T]]
  def create[T](entity: T): Future[Option[T]]
  def read[T](id: String): Future[Option[T]]
  def update[T](selector: List[T] => Boolean, entitiesUpdateFun: List[T] => List[T]): Future[Boolean]
  def delete[T](entityId: String): Future[Boolean]
}