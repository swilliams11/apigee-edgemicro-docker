# Deploy an Edgemicro proxy to Apigee Edge


You must export the following environment variables:
```
export ae_password=apigeepassword
export ae_username=apigeeusername
export ae_org=apigeeorg
```


### edge.json
The edge.json file contains the configuration (KVMs, Caches, Target Servers, products, developers apps, etc.) for deploying the proxy to Edge.

## Helpful to Know
You don't have to review these links, since I list all the commands to deploy the proxy.  But if you are interested in learning more about the config and deploy plugins, then you should read them.
* [apigee-config-maven-plugin](https://github.com/apigee/apigee-config-maven-plugin)
* [apigee-maven-deploy-plugin](https://github.com/apigee/apigee-deploy-maven-plugin)


# Apigee Edge

## You must update the following

* Update the jmeter/edgemicro_test.csv with the IP/hostname of the edgemicro kubernetes load balancer server IP

Update the following if you want to run the Jmeter tests
* Update the config.json file with the IP/hostname of the target server



## Deploy
Follow the steps below to deploy the proxy, and create the developer, product and app in Apigee Edge.

### Proxy
This will deploy:
* a proxy
* a developer
* a developer app
* an API product

### Deploy the proxy
Run this command to deploy the proxy.


```
cd edgemicroproxy/microproxy
mvn install -Ptest -Dusername=$ae_username -Dpassword=$ae_password \
                    -Dorg=$ae_org -Dauthtype=oauth -Dapigee.config.options=create
```
