package controllers

import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import models.JsonFormats._

import scala.concurrent.Future

object Users extends Controller {

  import db.DbEmulator._

  def createUser = Action.async(parse.json) {
    request =>
      request.body.validate[User].map {
        user =>
          collection[User].insert(user).map(_ => Created(s"User Created"))
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
