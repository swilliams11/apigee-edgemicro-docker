# Node.js backend for Microgateway testing
This is a backend server that returns 10KB payloads.

## Export the project ID
```
export PROJECT_ID=xxxx
```


## Build and Deploy

1. builds and deploys the docker container to GCP private container repo
```./dockerbuildk8s.sh
```

That the container was created. This is also executed in the script above.
```
gcloud container images list
```

2.  deploy to k8s
```
kubectl create -f nodebackend.yaml --validate=true --dry-run=false
```

3. get the IP for the service and test it

```
kubectl get svc
```

## Scale

### Scale Up
```
kubectl scale --current-replicas=2 --replicas=3 rc/nodejs-backend
```

### Scale Down
```
kubectl scale --replicas=2 rc/nodejs-backend
```

## Run locally

```
cd nodejsbackend
npm start
```

## Update Deployment
 ```
 kubectl apply -f nodebackend.yaml
 ```

## Test
```
curl http://IP:8080/10kb
curl http://IP:8080/100kb
```


## Clean Up
```
kubectl delete -f nodebackend.yaml
```
