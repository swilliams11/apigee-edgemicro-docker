# Summary
This directory contains the configuration files for a MG as a pass-through proxy. No plugins are configured here.

# Create the Docker image that does not include the oauth plugins
Execute the following commands from the `apigee-edgemicro-docker` folder.

1. Remove the oauth plugin from your `org-env-config.yaml` file (see example below).
2. execute `./dockerbuildk8snooauth.sh orgname environmentname`
  * you will see some errors the first time you execute this because it attempts to delete the microgateway-no-oauth image first from the local Docker as well as in GCP.

An excerpt of org-env-config.yaml file.
```
edgemicro:
  port: 8000
  max_connections: 4000
  config_change_poll_interval: 300
  logging:
    level: warn
    dir: /var/tmp
    to_console: false
    stats_log_interval: 60
    rotate_interval: 24
  plugins:
    sequence:

headers:
  x-forwarded-for: true
  x-forwarded-host: true
  x-request-id: true
  x-response-time: true
  via: true
oauth:
  allowNoAuthorization: true
  allowInvalidAuthorization: true
```

# Create the deployment
This will create the secrets, load balancer and the deployment.

```
kubectl create -f mgw-secret-no-oauth.yaml --validate=true --dry-run=false
```

# Update the deployment

Subsequent updates should use the following command.
This section describes how to perform a rolling update in GKE ([GKE rolling update docs](https://cloud.google.com/kubernetes-engine/docs/how-to/updating-apps)).  

Execute the following commands one at a time to test/deploy different configurations of the MG pod.


```
kubectl apply -f mgw-secret-no-oauth-r2.yaml
```

# Deployment status
Check the deployment status.

```
kubectl rollout status deployment edge-microgateway-no-oauth
```

# Included Files
* `mgw-secret-no-oauth.yaml` creates the service, secrets and deployment with all of the defaults. 100mcpu and 256 memory
* `mgw-secret-no-oauth-r2.yaml` 2 CPUs, 1024M 2 EM processes.


# Delete the deployment and service
The following command will delete all the components.  

```bash
kubectl delete -f mgw-secret-no-oauth.yaml
```

# Test the deployment
1. `kubectl get services` should return

```
NAME                         TYPE           CLUSTER-IP      EXTERNAL-IP      PORT(S)          AGE
edge-microgateway            LoadBalancer   1.1.2.2   1.1.1.2   8000:31540/TCP   22d
edge-microgateway-no-oauth   LoadBalancer   1.1.2.9    1.2.1.1  8000:32629/TCP   38d
```

2. Take the IP address from `edge-microgateway-no-oauth` and send the following curl command.

```
curl http://IP:8000/edgemicro_perftest/100kb -i
curl http://IP:8000/edgemicro_perftest/10kb -i
```
