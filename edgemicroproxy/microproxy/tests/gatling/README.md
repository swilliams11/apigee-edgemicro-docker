gatling-maven-plugin-demo
=========================

# Summary
This script runs performance test against Cloud Foundry Endpoints and the Apigee Service Broker.

# Initalization
1. Update the application.conf file located in the src/test/resources directory.

# Run From Command Line
Simple showcase of a maven project using the gatling-maven-plugin.

To test it out, simply execute the following command:
```
cd edgemicroproxy/microproxy/tests/gatling
mvn gatling:test -Dgatling.simulationClass=microgateway.EdgeMicrogatewayPerformanceTest
 ```

or simply:

```
mvn gatling:test
```

## View this documentation on

[Simulation Setup](https://gatling.io/docs/2.3/general/simulation_setup/) provides documentation on how to setup the simulation.
* There are two ways to think about Gatling tests
  * Number of [concurrent users](https://gatling.io/docs/2.3/general/simulation_setup/#injection)
  * Requests per second ([RPS](https://gatling.io/docs/2.3/general/simulation_setup/#throttling)).
