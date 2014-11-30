package controllers

import actions.{BasicAuth, UserInfo}
import models.Product
import models.Product._
import play.api.libs.json.Json
import play.api.mvc._

object Application extends Controller {

  def products = BasicAuth.withUser {
    case UserInfo(name, password) => Action(Ok(Json.toJson(Product.list)))
  }

}