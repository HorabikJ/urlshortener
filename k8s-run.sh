#!/bin/bash

kubectl delete namespace urlshortener-dev

docker image rm urlshortener-keycloak:k8s
docker build -t urlshortener-keycloak:k8s ./docker-containers/keycloak/k8s/

docker image rm urlshortener-app:k8s
docker build -t urlshortener-app:k8s .

docker image rm urlshortener-db:k8s
docker build -t urlshortener-db:k8s ./docker-containers/mysql/ 

kubectl create -f ./k8s/urlshortener-namespace.yaml

kubectl create -f ./k8s/db/urlshortener-db-deployment.yaml
kubectl create -f ./k8s/db/urlshortener-db-service.yaml
#todo replace sleep commands with readiness probes
sleep 10
kubectl create -f ./k8s/keycloak/urlshortener-keycloak-deployment.yaml
kubectl create -f ./k8s/keycloak/urlshortener-keycloak-service.yaml
sleep 10
kubectl create -f ./k8s/app/urlshortener-app-deployment.yaml
kubectl create -f ./k8s/app/urlshortener-app-service.yaml
