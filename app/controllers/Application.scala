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
        order => {
          def checkProductQuantityAvailability(existingProducts: List[Product]): Boolean = {
            order.products.forall(
              orderItem => existingProducts.find(_.id == orderItem.productId).exists(_.quantity - orderItem.preOrderedQuantity >= 0) //todo: reduce complexity of N^2
            )
          }

          collection[Product].update(
            _.map(p => p -> order.products.find(_.productId == p.id).map(_.preOrderedQuantity)).collect {
              case (product, Some(orderedQuantity)) => product.copy(quantity = product.quantity - orderedQuantity)
            },
            checkProductQuantityAvailability
          ).map {
            case true => Ok("%s, your order successfully accepted!" format userName.capitalize)
            case false => Conflict("Insufficient amount of one of the products")
          }
        }
      }.getOrElse(Future.successful(BadRequest("Invalid json")))
  }
}