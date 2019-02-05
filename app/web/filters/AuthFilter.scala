package com.wunder.pets.web.filters

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AuthFilter @Inject()(config: Configuration)(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

   if (requestHeader.path == "/hello_world") {
     nextFilter(requestHeader)
   }
   else {
    val result: Option[Boolean] = for {
      token <- requestHeader.headers.get(AuthFilter.X_PETS_TOKEN)
      configuredToken <- config.getString("pet.api.token")
    } yield token.matches(configuredToken)

    val unauthorized = Future.successful(Results.Unauthorized)

    result.map(matches => if (matches) nextFilter(requestHeader) else unauthorized).getOrElse(unauthorized)
    }
  }
}

object AuthFilter {
  // if you put this here there is ever only 1 instance instead of 1/class.
  val X_PETS_TOKEN = "X-Pets-Token"
}
