#!/bin/bash

docker rmi microgateway-no-oauth
#build the image
docker build --build-arg ORG="$1" --build-arg ENV="$2" -t microgateway-no-oauth .
#remove the image if it exists otherwise it will report an error (can be ignored)
docker rmi gcr.io/$PROJECT_ID/microgateway-no-oauth
docker tag microgateway-no-oauth:latest gcr.io/$PROJECT_ID/microgateway-no-oauth:latest
#remove the image if it exists otherwise it will report an error (can be ignored)
gcloud container images delete gcr.io/$PROJECT_ID/microgateway-no-oauth
#gcloud docker -- push gcr.io/$PROJECT_ID/microgateway-no-oauth:latest
docker push gcr.io/$PROJECT_ID/microgateway-no-oauth:latest
