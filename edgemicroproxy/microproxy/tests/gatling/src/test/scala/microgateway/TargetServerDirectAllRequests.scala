package microgateway

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import microgateway.TargetServerDirect._


class TargetServerDirectAllRequests extends Simulation {

  //configure the http object base url generically.
  val httpConf = http
    .baseURL("http://") // Here is the root for all relative URLs
    .disableCaching


  val scn1 = scenario("Direct To Target Server - 10kb Performance Test Scenario").exec(TargetServerDirect10kb.getRequest) // A scenario is a chain of requests and pauses
  val scn2 = scenario("Direct To Target Server - 100kb Performance Test Scenario").exec(TargetServerDirect100kb.getRequest) // A scenario is a chain of requests and pauses

  /*
 This setup is in terms of number of concurrent users.
 We don't know with this model the total number of requests per sec
  */
  setUp(
    scn1.inject(
      atOnceUsers(atOnceUsersConfig),
      constantUsersPerSec(constantUsersPerSecConfig) during(durationConfig minutes)
    ).protocols(httpConf),
    scn2.inject(
      atOnceUsers(atOnceUsersConfig),
      constantUsersPerSec(constantUsersPerSecConfig) during(durationConfig minutes)
    ).protocols(httpConf)
  )
}
