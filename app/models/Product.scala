package models

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

case class Product(name: String, price: BigDecimal, quantity: Int, id: String = UUID.randomUUID().toString)

object Product {
  def list: List[Product] = {
    List(Product("test", 23.4, 4), Product("test2", 623.4, 2))
  }

  implicit val productWrites: Writes[Product] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "price").write[BigDecimal] and
      (JsPath \ "quantity").write[Int] and
      (JsPath \ "id").write[String]
    )(unlift(Product.unapply))
}