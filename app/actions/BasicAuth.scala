package actions

import controllers.Application._
import db.DbEmulator
import models.User
import play.api.mvc.{Action, Result, Security}
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

object BasicAuth {
  private implicit def toUserInfoHelper(authHeaderContent: String): Object {def userInfo: Option[UserInfo]} = new {
    def userInfo: Option[UserInfo] = {
      authHeaderContent.split(" ").drop(1).headOption.flatMap { encoded =>
        new String(org.apache.commons.codec.binary.Base64.decodeBase64(encoded.getBytes)).split(":").toList match {
          case user :: password :: Nil => Some(UserInfo(user, password))
          case _ => None
        }
      }
    }
  }

  private def user(info: UserInfo) = {
    DbEmulator.collection[User].find(info.login)
  }

  def authenticatedAsync(action: String => Future[Result]) = Security.Authenticated(
    _.headers.get(AUTHORIZATION).flatMap(_.userInfo).map(user),
    _ => unauthorized
  )(userFuture => Action.async(
    userFuture.flatMap(_.map(user => action(user.email)).getOrElse(Future.successful(unauthorized)))
  ))

  private def unauthorized = {
    Unauthorized(views.html.defaultpages.unauthorized()).withHeaders("WWW-Authenticate" -> "Basic realm=\"Secured\"")
  }
}

case class UserInfo(login: String, password: String)