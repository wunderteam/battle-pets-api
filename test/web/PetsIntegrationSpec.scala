package com.wunder.pets.test.web

import java.util.UUID

import com.wunder.pets.test.support.AuthRequest
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.test.Helpers._
import support.IntegrationSpec

/**
  * add your integration spec here.
  * An integration test will fire up a whole play application in a real (or headless) browser
  */
class PetsIntegrationSpec extends IntegrationSpec  with AuthRequest {
  "Creating a pet" should {
    "return 404 if pet can not be found" in {
      val wsClient = app.injector.instanceOf[WSClient]
      val endpoint = s"http://localhost:$port/pets/${UUID.randomUUID()}"
      val response = await(authenticated(wsClient.url(endpoint)).get())

      response.status.mustBe(NOT_FOUND)
    }

    "create a pet" in {
      val wsClient = app.injector.instanceOf[WSClient]
      val endpoint = s"http://localhost:$port/pets"

      val expectedStrength = 42

      val data = Json.obj(
        "strength" -> expectedStrength,
        "name" -> "Bob"
      )

      val createResponse = await(authenticated(wsClient.url(endpoint)).post(data))

      createResponse.status.mustBe(CREATED)
      val json = Json.parse(createResponse.body)
      val createdId = (json \ "id").as[String]

      val response = await(authenticated(wsClient.url(s"$endpoint/$createdId")).get())

      println(response.body)
      response.status.mustBe(OK)
      val petJson = Json.parse(createResponse.body)
      val strength = (petJson \ "strength").as[Int]

      strength.mustBe(expectedStrength)
    }
  }
}
