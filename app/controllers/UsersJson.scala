package controllers

import com.google.inject.Inject
import controllers.constructors.CreateUserActionConstructor
import models.JsonFormats._
import models.User
import play.api.libs.json.JsValue
import play.api.mvc._

class UsersJson @Inject()(createUserAction: CreateUserActionConstructor) extends Controller {

  def createUser = createUserAction[JsValue](
    parse.json,
    (body: JsValue, createUserFun: createUserAction.ObjectToResultFun, parsingFailure) =>
      body.validate[User].map(createUserFun).getOrElse(parsingFailure)
  )
}
