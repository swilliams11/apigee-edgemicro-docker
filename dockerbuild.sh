#!/bin/bash

docker build --build-arg ORG="org" --build-arg ENV="env" --build-arg KEY="8axxx7" --build-arg SECRET="1cxxx0c" -t microgateway .
docker tag microgateway:latest gcr.io/$PROJECT_ID/microgateway:latest
gcloud docker -- push gcr.io/$PROJECT_ID/microgateway:latest
