package com.wunder.pets.test.support

import com.wunder.pets.web.filters.AuthFilter
import play.api.Application
import play.api.libs.ws.WSRequest

trait AuthRequest {
  implicit val app: Application

  def authenticated(req: WSRequest) = {
    val token = app.configuration.getString("pet.api.token")
    req.withHeaders((AuthFilter.X_PETS_TOKEN, token.getOrElse("")))
  }
}
