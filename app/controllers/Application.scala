package controllers

import actions.BasicAuth
import db.DbEmulator._
import models.JsonFormats._
import models.{Order, Product}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

object Application extends Controller {

  def products = BasicAuth.authenticatedAsync { _ =>
    collection[Product].list.map(Json.toJson(_)).map(Ok(_))
  }

  def makeOrder = BasicAuth.authenticatedAsync(parse.json) {
    (request, userName) =>
      request.body.validate[Order].map {
        order =>
          collection[Product].update(order.products).map(_ =>
            Ok("%s, your order successfully accepted!" format userName.capitalize)
          )
      }.getOrElse(Future.successful(BadRequest("Invalid json")))
  }
}