package controllers

import com.google.inject.Inject
import controllers.constructors.{MakeOrderActionConstructor, ProductActionConstructor}
import models.JsonFormats._
import models.Order
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

class ApplicationJson @Inject()(
                                 productActionConstructor: ProductActionConstructor,
                                 orderActionConstructor: MakeOrderActionConstructor
                                 ) extends Controller {

  def productJson = productActionConstructor(products => Results.Ok(Json.toJson(products)))

  def makeOrderFromJson = orderActionConstructor[JsValue](
    parse.json,
    (body: JsValue, orderFun: orderActionConstructor.ObjectToResultFun, failureFun) =>
      body.validate[Order].map(orderFun).getOrElse(failureFun)
  )
}