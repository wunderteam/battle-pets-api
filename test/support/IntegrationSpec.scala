package support

import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.Injector
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.jdbc.SQLActionBuilder
import slick.jdbc.SetParameter.SetUnit

import scala.concurrent.Await
import scala.concurrent.duration._

trait IntegrationSpec extends PlaySpec with OneServerPerSuite with BeforeAndAfterEach {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  // Not super concerned about this because it is just a test helper.
  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  override def beforeEach() = cleanDatabase(app.injector)


  private def cleanDatabase(injector: Injector) = {
    val dbConfig = injector.instanceOf[DatabaseConfigProvider].get[JdbcProfile]
    val truncatesFuture = dbConfig.db.run(
      sql"""SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name NOT IN ('schema_version');""".as[String]
    ).map {
      _.map { case tableName => SQLActionBuilder(List(s"ALTER TABLE $tableName DISABLE TRIGGER ALL;TRUNCATE TABLE $tableName;ALTER TABLE $tableName ENABLE TRIGGER ALL;"), SetUnit).asUpdate }
    }

    Await.result(truncatesFuture.flatMap(
      truncates =>
        dbConfig.db.run(
          DBIO.sequence(
            List(
              truncates
            ).flatten
          )
        )
    ), 5.seconds)
  }
}
