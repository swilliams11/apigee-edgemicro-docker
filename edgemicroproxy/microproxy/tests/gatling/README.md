gatling-maven-plugin-demo
=========================

# Summary
This script runs performance test against Cloud Foundry Endpoints and the Apigee Service Broker.

# Initalization
1. Update the application.conf file located in the src/test/resources directory.

You should update the following values:
```
apigee-conf {
  apigee-org = "apigeeorg"
  apigee-env = "env"
  apigee-domain = ".apigee.net"
  apigee-microgateway-domain = "IP_or_domainname"
  apigee-microgateway-port = "8000"
  apigee-microgateway-target-domain = "IP_or_domain"
  apigee-microgateway-target-port = "8080"
  apigee-apikey = "client_id"
  apigee-secret = "client_secret"
}

gatling-conf {
  at-once-users = 1
  constant-users-per-sec = 2
  duration = 1
}
```

# Run From Command Line
Simple showcase of a maven project using the gatling-maven-plugin.

To test it out, simply execute the following command:
This runs both 10kb, and 100kb requests in parallel
```
cd edgemicroproxy/microproxy/tests/gatling
mvn gatling:test -Dgatling.simulationClass=microgateway.EdgeMicrogatewayPerformanceTestAllRequests
 ```

or simply:

```
mvn gatling:test
```

## Run 10kb scenario only
```
cd edgemicroproxy/microproxy/tests/gatling
mvn gatling:test -Dgatling.simulationClass=microgateway.EdgeMicrogatewayPerformanceTest10kbOnly
 ```
 
## Run 100kb scenario only
```
mvn gatling:test -Dgatling.simulationClass=microgateway.EdgeMicrogatewayPerformanceTest100kbOnly
```

## Run target server direct:
This executes performance tests directly to the Node.js target server with 10kb and 100kb requests.  

```
mvn gatling:test -Dgatling.simulationClass=microgateway.TargetServerDirectAllRequests
```

## Run target server direct 10kb request:
This executes performance tests directly to the Node.js target server.  

```
mvn gatling:test -Dgatling.simulationClass=microgateway.TargetServerDirect10kb
```

## Run target server direct 100kb request:
This executes performance tests directly to the Node.js target server.  

```
mvn gatling:test -Dgatling.simulationClass=microgateway.TargetServerDirect100kb
```


## View this documentation on

[Simulation Setup](https://gatling.io/docs/2.3/general/simulation_setup/) provides documentation on how to setup the simulation.
* There are two ways to think about Gatling tests
  * Number of [concurrent users](https://gatling.io/docs/2.3/general/simulation_setup/#injection)
  * Requests per second ([RPS](https://gatling.io/docs/2.3/general/simulation_setup/#throttling)).
