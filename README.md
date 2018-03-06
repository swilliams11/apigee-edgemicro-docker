# Apigee Edge Microgateway on Docker & Kubernetes
This project describes how you can deploy Apigee Edge Microgateway on Kubernetes in GCP.

This repository has:
* nodejs backend that you can deploy to K8S
* edgemicro proxy that you can use to test edgemicro
* TODO - Jmeter tests
* TODO - Gatling tests

### Prerequisites
1. Docker
Please refer to this page to install docker in your machine
https://www.docker.com/products/docker-toolbox

### Prerequisites
[outside of docker]
1. Install Edge microgateway
```
npm install -g edgemicro
```

2. Initialize Edge microgateway
```
edgemicro init
```

3. Configure Edge microgateway
```
edgemicro configure -o "your-orgname" -e "your-envname" -u "your-username"
```

NOTE: OPDK users should use "edgemicro private configure".

At the end of a successful configuration, you will see a file in the ~/.edgemicro/{org}-{env}-config.yaml as well as a key and secret. The key maps to EDGEMICRO_KEY and the secret maps to EDGEMICRO_SECRET in the following section.

### Getting Started
1. Clone the project
```
git clone https://github.com/srinandan/apigee-edgemicro-docker.git
```

2. Switch directory
```
cd apigee-edgemicro-docker
```

4. Copy the {org}-{env}-config.yaml to the current folder (from pre-reqs). Edit the Dockerfile with the correct file name.

5. Build the docker image using following command:
```
docker build --build-arg ORG="your-orgname" --build-arg ENV="your-env" --build-arg KEY="bxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx2" --build-arg SECRET="exxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0" -t microgateway .
```

6. This will create a image apigee-edgemicro and you can see the images using command:
```
docker images
```

7. To start docker
```
docker run -d -p 8000:8000 -e EDGEMICRO_ORG="your-orgname" -e EDGEMICRO_ENV="your-env" -e EDGEMICRO_KEY="bxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx2" -e  EDGEMICRO_SECRET="exxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0" -P -it microgateway
```

### Operationalizing Edge Microgateway (MG)
#### Configuration File
Each MG instance can expose a different set of APIs. Furthermore, each MG container can load a different set of plugins depending on the needs of the API.

All of this is controlled via the configuration yaml file. Generate the config yaml outside of the docker image. Edit the configuration file as necessary (enable proxies, plugins etc.) before you build the docker image. You'll want to store the configuration file in a source code repo.

#### Custom plugins
If the MG instance uses custom plugins, one way is to package those custom plugins as npm modules (private repo or public repo). Then the installation of MG can be done as:
```
npm install -g edgemico plugin-1 plugin-2
```

#### A Sample configuration file
```
edge_config:
.
.
. omitted for brevity
edgemicro:
  port: 7000
  max_connections: 1000
  max_connections_hard: 5000
  restart_sleep: 500
  restart_max: 50
  max_times: 300
  config_change_poll_interval: 600
  logging:
    level: error
    dir: /var/tmp
    stats_log_interval: 60
    rotate_interval: 24
  plugins:
    sequence:
      - oauth
      - plugin-1
      - plugin-2
.
.
. omitted for brevity
```

### Deploying to Kubernetes (GKE)
#### Set your project id
```
export PROJECT_ID=xxxx
```

#### Set GCP Project and create the cluster
Set the project config.
```
gcloud config set project $PROJECT_ID
gcloud config set compute/zone us-central1-a
gcloud container clusters create microgatewaycluster1
gcloud container clusters get-credentials microgatewaycluster1

```

#### Tag the docker image

```
docker tag microgateway gcr.io/$PROJECT_ID/microgteway:latest
```

#### Convert Edge Microgateway credentials to base64
Convert each of these values into base64. This will help store those credentials into k8s secrets.
```
echo -n "org" | base64
echo -n "env" | base64
echo -n "key" | base64
echo -n "secret" | base64
```
Use the results in the configuration below.

#### Deploy Your Docker Image to Google's private container repository
In order for Kubernetes to start the MG container, it should be accessible via a public/private container repository to which Kubernetes has access.  These steps can also be completed with the `dockerbuildk8s.sh` file, but you still need to complete the first step below before you execute that script.

1. Enable the container registry for your project by following the steps [here](https://cloud.google.com/container-registry/docs/quickstart#before-you-begin).

2. [Push](https://cloud.google.com/container-registry/docs/pushing-and-pulling) your container to the private repository.

```
gcloud docker -- push gcr.io/$PROJECT_ID/microgateway:latest
```

3. Check that your container was uploaded successfully.
```
gcloud container images list
```

Console
```
NAME
gcr.io/YOUR_GCP_PROJECT_ID/microgateway
Only listing images in gcr.io/YOUR_GCP_PROJECT_ID. Use --repository to list images in other repositories.
```

**Note:** You may may need to install the docker credential handler.
https://cloud.google.com/container-registry/docs/advanced-authentication
https://github.com/GoogleCloudPlatform/docker-credential-gcr


#### Sample Configuration

```
apiVersion: v1
kind: Secret
metadata:
  name: mgwsecret
type: Opaque
data:
  mgorg: xxx=
  mgenv: xxx==
  mgkey: Oxxxw==
  mgsecret: Mxxxw==
---
apiVersion: v1
kind: Service
metadata:
  name: edge-microgateway
  labels:
    app: edge-microgateway
spec:
  ports:
  - port: 8000
    name: http
  selector:
    app: edge-microgateway
---
apiVersion: v1
kind: Pod
metadata:
  name: edge-microgateway
spec:
  restartPolicy: Never
  containers:
    - name: edge-microgateway
      image: gcr.io/YOUR_PROJECT_ID_HERE/microgateway:latest
      ports:
        - containerPort: 8000
      env:
        - name: EDGEMICRO_ORG
          valueFrom:
            secretKeyRef:
              name: mgwsecret
              key: mgorg
        - name: EDGEMICRO_ENV
          valueFrom:
            secretKeyRef:
              name: mgwsecret
              key: mgenv
        - name: EDGEMICRO_KEY
          valueFrom:
            secretKeyRef:
              name: mgwsecret
              key: mgkey
        - name: EDGEMICRO_SECRET
          valueFrom:
            secretKeyRef:
              name: mgwsecret
              key: mgsecret
        - name: EDGEMICRO_CONFIG_DIR
          value: /home/microgateway/.edgemicro
```

Create the resources. This will create the K8S secrets, the Pod with Microgateway running on it and the load-balancer service so that it is publicly accessible.  
```
kubectl create -f mgw-secret.yaml --validate=true --dry-run=false
```

#### Testing the deployment
* Use ```kubectl get svc``` to get the external IP address. For ex:
```
NAME           TYPE           CLUSTER-IP     EXTERNAL-IP      PORT(S)          AGE
kubernetes     ClusterIP      10.xx.xxx.x    <none>           443/TCP          17d
microgateway   LoadBalancer   10.xx.xxx.xx   xx.xxx.xxx.xxx   8000:30486/TCP   17d
```

* Test via curl
```
curl -v http://xx.xxx.xxx.xxx:8000/httpbin -v


*   Trying xx.xxx.xxx.xxx...
* TCP_NODELAY set
* Connected to xx.xxx.xxx.xxx (xx.xxx.xxx.xxx) port 8000 (#0)
> GET /httpbin HTTP/1.1
> Host: xx.xxx.xxx.xxx:8000
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 401 Unauthorized
< content-type: application/json
< Date: Sun, 10 Dec 2017 22:17:21 GMT
< Connection: keep-alive
< Content-Length: 84
<
* Connection #0 to host 35.193.218.139 left intact
{"error":"missing_authorization","error_description":"Missing Authorization header"}%
```

#### Delete the deployment and service
The following command will delete all the components.  
```
kubectl delete -f mgw-secret.yaml
```

You can also execute
```./k8scleanup.sh```





### License
Apache 2.0
