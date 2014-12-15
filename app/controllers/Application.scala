package controllers

import actions.BasicAuth
import com.google.inject.Inject
import mappers.CRUDController
import models.JsonFormats._
import models.{Order, Product}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

class Application @Inject()(crudController: CRUDController, authAction: BasicAuth) extends Controller {

  def products = authAction.authenticatedAsync { _ =>
    crudController.list[Product].map(Json.toJson(_)).map(Ok(_))
  }

  def makeOrder = authAction.authenticatedAsync(parse.json) {
    (request, userName) =>
      request.body.validate[Order].map {
        order => {
          def checkProductQuantityAvailability(existingProducts: List[Product]): Boolean = {
            order.products.forall(
              orderItem => existingProducts.find(_.id == orderItem.productId).exists(_.quantity - orderItem.preOrderedQuantity >= 0) //todo: reduce complexity of N^2
            )
          }

          crudController.update[Product](
            checkProductQuantityAvailability,
            _.map(p => p -> order.products.find(_.productId == p.id).map(_.preOrderedQuantity)).collect {
              case (product, Some(orderedQuantity)) => product.copy(quantity = product.quantity - orderedQuantity)
            }
          ).map {
            case true => Ok("%s, your order successfully accepted!" format userName.capitalize)
            case false => Conflict("Insufficient amount of one of the products")
          }
        }
      }.getOrElse(Future.successful(BadRequest("Invalid json")))
  }
}