#!/bin/bash

docker build -t nodejs-backend .
docker tag nodejs-backend:latest gcr.io/$PROJECT_ID/nodejs-backend:latest
gcloud docker -- push gcr.io/$PROJECT_ID/nodejs-backend:latest
gcloud container images list
