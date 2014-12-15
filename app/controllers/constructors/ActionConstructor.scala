package controllers.constructors

import actions.BasicAuth
import com.google.inject.Inject
import mappers.CRUDController
import models.{Order, Product, User}
import play.api.http.Writeable
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._

import scala.concurrent.Future

trait InboundActionConstructor[T] {
  type ObjectToResultFun = T => Future[Result]
  type UnWrapFailureResult = Future[Result]

  def apply[R: Writeable](parser: BodyParser[R], unWrapper: (R, ObjectToResultFun, => UnWrapFailureResult) => Future[Result]): EssentialAction
}

trait OutboundActionConstructor[T] {
  def apply(render: T => Result): EssentialAction
}


class ProductActionConstructor @Inject()(
                                          crudController: CRUDController,
                                          authAction: BasicAuth
                                          ) extends OutboundActionConstructor[List[Product]] {
  def apply(render: List[Product] => Result) =
    authAction.authenticatedAsync { _ =>
      crudController.list[Product].map(render)
    }
}

class MakeOrderActionConstructor @Inject()(
                                            crudController: CRUDController,
                                            authAction: BasicAuth
                                            ) extends InboundActionConstructor[Order] {
  def apply[R: Writeable](parser: BodyParser[R], unWrapper: (R, ObjectToResultFun, => UnWrapFailureResult) => Future[Result]): EssentialAction =
    authAction.authenticatedAsync(parser) {
      (request, userName) => unWrapper(request.body, processOrder(userName), failureResult)
    }

  private def failureResult[R]: Future[Result] = {
    Future.successful(Results.BadRequest("Invalid order format"))
  }

  private def processOrder(userName: String)(order: Order): Future[Result] = {
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
      case true => Results.Ok("%s, your order successfully accepted!" format userName.capitalize)
      case false => Results.Conflict("Insufficient amount of one of the products")
    }
  }
}

class CreateUserActionConstructor @Inject()(crud: CRUDController) extends InboundActionConstructor[User] {
  def apply[R: Writeable](parser: BodyParser[R], unWrapper: (R, ObjectToResultFun, => UnWrapFailureResult) => Future[Result]): EssentialAction =
    Action.async(parser) {
      request => unWrapper(request.body, processUserCreation, failure)
    }

  private def failure[R]: Future[Result] = {
    Future.successful(Results.BadRequest("invalid json"))
  }

  private def processUserCreation[R](user: User): Future[Result] = {
    crud.create(user).map(_ => Results.Created(s"User Created"))
  }
}