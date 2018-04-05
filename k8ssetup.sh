#!/bin/bash
kubectl run edge-microgateway --image=gcr.io/$PROJECT_ID/microgateway --port=8000 --replicas=2
kubectl expose deployment edge-microgateway --type=LoadBalancer
kubectl get services
