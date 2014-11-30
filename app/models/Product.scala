package models

import java.util.UUID

import actors.Contexts
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

import scala.concurrent.Future

case class Product(name: String, price: BigDecimal, quantity: Int, id: String = UUID.randomUUID().toString)

object Product {
  def list: Future[List[Product]] = Future {
    Thread.sleep(2000)
    List(Product("test", 23.4, 4), Product("test2", 623.4, 2))
  }(Contexts.dbEmulationExecutionContext)

  implicit val productWrites: Writes[Product] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "price").write[BigDecimal] and
      (JsPath \ "quantity").write[Int] and
      (JsPath \ "id").write[String]
    )(unlift(Product.unapply))
}