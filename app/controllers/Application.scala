package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import utils._

import views._

case class FBSignedRequest(signedRequest:String)

object Application extends Controller {
  val secret = "YOUR_APP_SECRET"

  val fbSignedRequestForm:Form[FBSignedRequest] = Form(
    mapping(
      "signed_request"->text
    )(FBSignedRequest.apply)(FBSignedRequest.unapply)
  )

  def index = Action {implicit request =>
    fbSignedRequestForm.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => {
        Ok(views.html.index("Unable to process the request"))
      },
      
      // We got a valid User value, display the summary
      fbSignedRequest => {
        val envelope = FacebookUtils.parseSignedRequest(fbSignedRequest.signedRequest, secret)
        Ok(views.html.index(envelope.toString))
      }
    )
  }
}