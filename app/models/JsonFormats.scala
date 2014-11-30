package models

import play.api.libs.json.Json

object JsonFormats {
  implicit val productJson = Json.format[Product]
  implicit val userJson = Json.format[User]
}
