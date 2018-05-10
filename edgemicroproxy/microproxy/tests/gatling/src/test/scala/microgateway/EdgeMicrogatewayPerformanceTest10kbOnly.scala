package microgateway

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random
import microgateway.EdgeMicrogatewayPerformanceTest._



class EdgeMicrogatewayPerformanceTest10kbOnly extends Simulation {

  //configure the http object base url generically.
  val httpConf = http
    .baseURL("http://") // Here is the root for all relative URLs
    .disableCaching


  val scn10kbAccessToken = scenario("10kb Performance Test Scenario").exec(PerformanceTest10kbAccessToken.get10KBRequest) // A scenario is a chain of requests and pauses

  /*
  This setup is in terms of number of concurrent users.
  We don't know with this model the total number of requests per sec
   */
  setUp(
    scn10kbAccessToken.inject(
      atOnceUsers(atOnceUsersConfig),
      constantUsersPerSec(constantUsersPerSecConfig) during(durationConfig minutes)
    ).protocols(httpConf)
  )

}
