#!/bin/bash

docker build -t urlshortener-app:latest . 
docker build -t urlshortener-db:latest ./mysql/ 

kubectl create -f ./k8s/urlshortener-namespace.yaml
kubectl create -f ./k8s/urlshortener-db-pod.yaml
kubectl create -f ./k8s/urlshortener-db-service.yaml
kubectl create -f ./k8s/urlshortener-app-pod.yaml
kubectl create -f ./k8s/urlshortener-app-service.yaml
