package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, Silhouette}
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.Account
import play.api.data.Form
import play.api.mvc._
import play.api.data.Forms._
import utils.{AuthEnv, AuthenticatorServiceImpl, UserService}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global


class Auth @Inject() (
  val controllerComponents: ControllerComponents,
  authenticator: AuthenticatorServiceImpl,
  userService: UserService,
  val silhouette: Silhouette[AuthEnv])(
  val ex: ExecutionContext)
extends BaseController {

  def signin = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.signin())
  }

  val signInForm = Form(mapping(
    "login" -> nonEmptyText,
    "password" -> nonEmptyText
  )(Credentials.apply)(Credentials.unapply))

  def authenticate = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
//    Redirect(controllers.routes.Application.listProjects())
    signInForm.bindFromRequest.fold(
      //      formWithErrors => Future.successful(Redirect(controllers.routes.Auth.signin().toString)),
      formWithErrors => Future.successful(Ok("Error")),
      formData => {
        val Credentials(identifier, password) = formData
        val entryUri = request.session.get("ENTRY_URI")
        val targetUri: String = entryUri.getOrElse(routes.Application.listProjects.toString)
        authenticator.authenticate(identifier, password).flatMap { case Account(user, role) =>
          val loginInfo = LoginInfo(providerID = "sdfs", providerKey = user)
          userService.retrieve(loginInfo).flatMap {
            case Some(user) => for {
              authenticator <- silhouette.env.authenticatorService.create(loginInfo)
              cookie <- silhouette.env.authenticatorService.init(authenticator)
              result <- silhouette.env.authenticatorService.embed(cookie, Redirect(targetUri).withSession(request.session - "ENTRY_URI"))
            } yield {
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              result
            }
            case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
          }
        }.recover {
          case e: ProviderException => Redirect(routes.Auth.signin)
        }
      }
    )
  }

  def signout = silhouette.SecuredAction { implicit request: SecuredRequest[AuthEnv, AnyContent] =>
    Redirect(controllers.routes.Auth.signin())
  }
}
