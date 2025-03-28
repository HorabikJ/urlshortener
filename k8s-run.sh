#!/bin/bash

kubectl delete namespace urlshortener-dev

docker image rm urlshortener-keycloak
docker build -t urlshortener-keycloak:latest ./docker-containers/keycloak

docker image rm urlshortener-backend
docker build -t urlshortener-backend:latest .

docker image rm urlshortener-db
docker build -t urlshortener-db:latest ./docker-containers/mysql/ 

kubectl create -f ./k8s/urlshortener-namespace.yaml
kubectl create -f ./k8s/db/urlshortener-db-deployment.yaml
kubectl create -f ./k8s/db/urlshortener-db-service.yaml
sleep 10
kubectl create -f ./k8s/keycloak/urlshortener-keycloak-deployment.yaml
kubectl create -f ./k8s/keycloak/urlshortener-keycloak-service.yaml
sleep 10
kubectl create -f ./k8s/backend/urlshortener-backend-deployment.yaml
kubectl create -f ./k8s/backend/urlshortener-backend-service.yaml
