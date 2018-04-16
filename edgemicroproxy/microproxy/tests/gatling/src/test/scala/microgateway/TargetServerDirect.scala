package microgateway

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._


object TargetServerDirect extends Simulation {
  //fetch values from application.conf file
  val atOnceUsersConfig = ConfigFactory.load().getInt("gatling-conf.at-once-users")
  val constantUsersPerSecConfig = ConfigFactory.load().getInt("gatling-conf.constant-users-per-sec")
  val durationConfig = ConfigFactory.load().getInt("gatling-conf.duration")
  val targetDomain = ConfigFactory.load().getString("apigee-conf.apigee-microgateway-target-domain")
  val targetPort = ConfigFactory.load().getString("apigee-conf.apigee-microgateway-target-port")

  val jwt = null
  /**
    * Create the domain name as a string
    * @return
    */
  def getDomain(): String = {
    return "http://" + targetDomain + ":" + targetPort
  }

  //configure the http object base url generically.
  val httpConf = http
    .baseURL("http://") // Here is the root for all relative URLs


  object TargetServerDirect10kb {

    val getRequest = {
      exec(
        repeat(100, "i"){
          //this line labels each request independently
          //exec(http("100KB Get Request ${i}").get(getDomain() + "/edgemicro_perftest/100kb")
          exec(http("10KB Get Request").get(getDomain() + "/10kb")
            //.header("x-api-key", apigeeApikey)
          )
        }
      )
    }
  }

  object TargetServerDirect100kb {

    val getRequest = {
      exec(
        repeat(100, "i"){
          //this line labels each request independently
          //exec(http("100KB Get Request ${i}").get(getDomain() + "/edgemicro_perftest/100kb")
          exec(http("100KB Get Request").get(getDomain() + "/100kb")
            //.header("x-api-key", apigeeApikey)
          )
        }
      )
    }
  }


  /*val scn1 = scenario("Direct To Target Server - 10kb Performance Test Scenario").exec(TargetServerDirect10kb.getRequest) // A scenario is a chain of requests and pauses
  val scn2 = scenario("Direct To Target Server - 100kb Performance Test Scenario").exec(TargetServerDirect100kb.getRequest) // A scenario is a chain of requests and pauses
*/

  /*
  setUp(
    scn1.inject(
      atOnceUsers(atOnceUsersConfig),
      constantUsersPerSec(constantUsersPerSecConfig) during(durationConfig minutes)
    ).protocols(httpConf),
    scn2.inject(
      atOnceUsers(atOnceUsersConfig),
      constantUsersPerSec(constantUsersPerSecConfig) during(durationConfig minutes)
    ).protocols(httpConf)
  )*/
}
