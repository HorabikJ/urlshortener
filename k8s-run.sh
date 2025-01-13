#!/bin/bash

docker build -t urlshortener-app:latest . 
docker build -t urlshortener-db:latest ./mysql/ 

kubectl create -f ./k8s/urlshortener-namespace.yaml
kubectl create -f ./k8s/urlshortener-db-deployment.yaml
kubectl create -f ./k8s/urlshortener-db-service.yaml
kubectl create -f ./k8s/urlshortener-app-deployment.yaml
kubectl create -f ./k8s/urlshortener-app-service.yaml
