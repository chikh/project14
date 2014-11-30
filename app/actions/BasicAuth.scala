package actions

import controllers.Application._
import play.api.mvc.{AnyContent, Action, Security}

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

  def withUser(action: UserInfo => Action[AnyContent]) = Security.Authenticated(
    _.headers.get(AUTHORIZATION).flatMap(_.userInfo),
    _ => Unauthorized(views.html.defaultpages.unauthorized()).withHeaders("WWW-Authenticate" -> "Basic realm=\"Secured\"")
  )(action)
}

case class UserInfo(name: String, password: String)