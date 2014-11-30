package controllers

import actions.BasicAuth
import db.DbEmulator._
import models.Product
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._
import models.JsonFormats._

object Application extends Controller {

  def products = BasicAuth.authenticatedAsync { _ =>
      collection[Product].list.map(Json.toJson(_)).map(Ok(_))
  }

}