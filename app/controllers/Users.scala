package controllers

import com.google.inject.Inject
import mappers.CRUDController
import models.JsonFormats._
import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

class Users @Inject()(crudController: CRUDController) extends Controller {

  def createUser = Action.async(parse.json) {
    request =>
      request.body.validate[User].map {
        user =>
          crudController.create(user).map(_ => Created(s"User Created"))
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
