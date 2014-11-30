package controllers

import actions.{BasicAuth, UserInfo}
import models.Product
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._

object Application extends Controller {

  def products = BasicAuth.withUser {
    case UserInfo(name, password) => Action.async(Product.list.map(Json.toJson(_)).map(Ok(_)))
  }

}