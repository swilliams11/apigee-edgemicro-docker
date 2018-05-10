# Jenkins Build Script
This folder contains the Jenkins pipeline that executes the Gatling performance
tests on a GCP compute instance.  

## Jenkins Build Script
TODO

## Terraform
The `main.tf` file contains the Terraform script to create a GCP compute instance,
which will be used to execute the performance tests.
You must include the `account.json` file with a JSON key downloaded from your
GCP project with sufficient permissions to create a new instance.

You can execute the Terraform plan and apply the changes by
1) installing Terraform or
2) installing and running Terraform in a Docker container.

### Terraform installed locally
Execute the following commands once Terraform is installed.

```
terraform init
terraform plan
terraform apply
```

#### Teardown
```
terraform destroy
```
### Terraform from Docker
Run the following commands if you have docker installed. There is no need to run init.

`-v` maps the current  directory to the current directory in the Docker container.
`-w` sets the working directory to the same dir of -v.

```
docker run -v `pwd`:`pwd` -w `pwd` -i -t --rm hashicorp/terraform:light init
docker run -v `pwd`:`pwd` -v `pwd`:/tmp -w `pwd` -i -t --rm hashicorp/terraform:light plan
docker run -v `pwd`:`pwd` -w `pwd` -i -t --rm hashicorp/terraform:light apply
```

```
docker exec -ti terraform sh -c "echo a && echo b"
```

#### Teardown
```
docker run -v `pwd`:`pwd` -w `pwd` -i -t --rm hashicorp/terraform:light destroy
```
