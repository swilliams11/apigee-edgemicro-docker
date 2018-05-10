package microgateway

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random


object EdgeMicrogatewayPerformanceTest extends Simulation {
  //fetch values from application.conf file
  val apigeeMicrogatewayDomain = ConfigFactory.load().getString("apigee-conf.apigee-microgateway-domain")
  val apigeeMicrogatewayPort = ConfigFactory.load().getString("apigee-conf.apigee-microgateway-port")
  val apigeeApikey = ConfigFactory.load().getString("apigee-conf.apigee-apikey")
  val apigeeSecret = ConfigFactory.load().getString("apigee-conf.apigee-secret")
  val apigeeOrg = ConfigFactory.load().getString("apigee-conf.apigee-org")
  val apigeeEnv = ConfigFactory.load().getString("apigee-conf.apigee-env")
  val atOnceUsersConfig = ConfigFactory.load().getInt("gatling-conf.at-once-users")
  val constantUsersPerSecConfig = ConfigFactory.load().getInt("gatling-conf.constant-users-per-sec")
  val durationConfig = ConfigFactory.load().getInt("gatling-conf.duration")
  val jwt = null
  /**
    * Create the domain name as a string
    * @return
    */
  def getDomain(): String = {
    return "http://" + apigeeMicrogatewayDomain + ":" + apigeeMicrogatewayPort
  }

  def getApigeeTokenDomain(): String = {
    return "https://" + apigeeOrg + "-" + apigeeEnv + ".apigee.net"
  }

  //configure the http object base url generically.
  val httpConf = http
    .baseURL("http://") // Here is the root for all relative URLs
    .disableCaching

  /**
    * This object sends two requests to random endpoints.
    * Each user is has its own Hello object that it executes.
    */
  object PerformanceTest10kbAccessToken {

    /*exec {
      session => session.set("auth", "")

    }*/
    val get10KBRequest = {
      //doIfEqualsOrElse(session => session("auth").as[String], "") {
      //print("auth value not found in session")
      exec(
        http("oauth token request").post(getApigeeTokenDomain() + "/edgemicro-auth/token")
          //.header("Content-type", "application/json")
          .body(
            StringBody("""{"client_id": """" + apigeeApikey + """","client_secret":"""" + apigeeSecret + """","grant_type": "client_credentials" }""")
          ).asJSON
          .check(jsonPath("$.token").find.saveAs("auth"))
      ).pause(100 milliseconds).exec(
        repeat(100, "i"){
          //this line labels each request independently
          //exec(http("10KB Get Request ${i}").get(getDomain() + "/edgemicro_perftest/10kb")
          exec(http("10KB Get Request").get(getDomain() + "/edgemicro_perftest/10kb")
              //.header("x-api-key", apigeeApikey)
              .header("Authorization", "Bearer ${auth}")
          )
        }
      )


     /* } {
          print("auth value found in session")
          exec(http("10KB Get Request 1").get(getDomain() + "/edgemicro_perftest/10kb")
            .header("x-api-key", apigeeApikey)
            .header("Authorization", "Bearer ${auth}")
          )
            .pause(250 milliseconds)

        }*/

    }
  }

  object PerformanceTest100kbAccessToken {

    val get100KBRequest = {
      exec(
        http("oauth token request").post(getApigeeTokenDomain() + "/edgemicro-auth/token")
          //.header("Content-type", "application/json")
          .body(
          StringBody("""{"client_id": """" + apigeeApikey + """","client_secret":"""" + apigeeSecret + """","grant_type": "client_credentials" }""")
        ).asJSON
          .check(jsonPath("$.token").find.saveAs("auth"))
      ).pause(100 milliseconds).exec(
        repeat(100, "i"){
          //this line labels each request independently
          //exec(http("100KB Get Request ${i}").get(getDomain() + "/edgemicro_perftest/100kb")
          exec(http("100KB Get Request").get(getDomain() + "/edgemicro_perftest/100kb")
            //.header("x-api-key", apigeeApikey)
            .header("Authorization", "Bearer ${auth}")
          )
        }
      )
    }
  }

}
