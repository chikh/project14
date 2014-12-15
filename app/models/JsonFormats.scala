package models

import play.api.libs.json.Json

object JsonFormats {
  implicit val productJsonFormat = Json.format[Product]
  implicit val userJson = Json.format[User]
  implicit val orderedProductJson = Json.format[OrderedProduct]
  implicit val orderJson = Json.format[Order]
}
