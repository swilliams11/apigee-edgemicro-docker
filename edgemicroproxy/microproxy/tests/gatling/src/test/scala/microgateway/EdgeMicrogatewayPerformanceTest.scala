package microgateway

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random


class EdgeMicrogatewayPerformanceTest extends Simulation {
  //fetch values from application.conf file
  val apigeeMicrogatewayDomain = ConfigFactory.load().getString("apigee-conf.apigee-microgateway-domain")
  val apigeeMicrogatewayPort = ConfigFactory.load().getString("apigee-conf.apigee-microgateway-port")
  val atOnceUsersConfig = ConfigFactory.load().getInt("gatling-conf.at-once-users")
  val constantUsersPerSecConfig = ConfigFactory.load().getInt("gatling-conf.constant-users-per-sec")
  val durationConfig = ConfigFactory.load().getInt("gatling-conf.duration")


  /**
    * Create the domain name as a string
    * @return
    */
  def getDomain(): String = {
    return "http://" + apigeeMicrogatewayDomain + ":" + apigeeMicrogatewayPort
  }

  //configure the http object base url generically.
  val httpConf = http
    .baseURL("http://") // Here is the root for all relative URLs

  /**
    * This object sends two requests to random endpoints.
    * Each user is has its own Hello object that it executes.
    */
  object PerformanceTest10KB {

    val get10KBRequest = {
      exec(http("10KB Get Request 1").get(getDomain() + "/edgemicro_perftest/10kb"))
        .pause(250 milliseconds)
    }

    val get10KBRequest2 = {
      exec(http("10KB Get Request 2").get(getDomain() + "/edgemicro_perftest/10kb"))
        .pause(250 milliseconds)
    }
  }

  object PerformanceTest100KB {

    val get100KBRequest = {
      exec(http("100kb Get Request 1").get(getDomain() + "/edgemicro_perftest/100kb"))
        .pause(250 milliseconds)
    }

    val get100KBRequest2 = {
      exec(http("100kb Get Request 2").get(getDomain() + "/edgemicro_perftest/100kb"))
        .pause(250 milliseconds)
    }
  }

  val scn = scenario("10kb Performance Test Scenario").exec(PerformanceTest10KB.get10KBRequest, PerformanceTest10KB.get10KBRequest2) // A scenario is a chain of requests and pauses

  /*
  This setup is in terms of number of concurrent users.
  We don't know with this model the total number of requests per sec
   */
  setUp(
    scn.inject(
      atOnceUsers(atOnceUsersConfig),
      constantUsersPerSec(constantUsersPerSecConfig) during(durationConfig minutes)
    ).protocols(httpConf))

}
