package actions

import controllers.Application._
import db.DbEmulator
import models.User
import play.api.mvc._
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

  def authenticatedAsync[A](bodyParser: BodyParser[A])(action: (Request[A], String) => Future[Result]) =
    authenticatedWithFutureUser(userFuture => Action.async(bodyParser)(parsed =>
      actionWithUnwrappedUser(action(parsed, _), userFuture)
    ))


  def authenticatedAsync(action: String => Future[Result]) =
    authenticatedWithFutureUser(userFuture => Action.async(
      actionWithUnwrappedUser(action, userFuture)
    ))

  private def unauthorized = {
    Unauthorized(views.html.defaultpages.unauthorized()).withHeaders("WWW-Authenticate" -> "Basic realm=\"Secured\"")
  }

  private def validUserAccount(info: UserInfo) = {
    DbEmulator.collection[User].find(info.login).map(_.filter(_.password == info.password))
  }


  private def authenticatedWithFutureUser[A]: ((Future[Option[User]]) => EssentialAction) => EssentialAction = {
    Security.Authenticated(
      _.headers.get(AUTHORIZATION).flatMap(_.userInfo).map(validUserAccount),
      _ => unauthorized
    )
  }

  private def actionWithUnwrappedUser[A](action: String => Future[Result], userFuture: Future[Option[User]]): Future[Result] = {
    userFuture.flatMap(_.map(user => action(user.email)).getOrElse(Future.successful(unauthorized)))
  }
}

case class UserInfo(login: String, password: String)