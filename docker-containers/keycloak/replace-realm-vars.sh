#!/bin/bash

rm ./realm/*.json
cp ./urlshortener-keycloak-realm-template.json ./realm/urlshortener-keycloak-realm.json

appBaseUrl=$1
keycloakInternalBaseUrl=$2
keycloakExternalBaseUrl=$3
urlshortenerClientSecret=$4
gmailAppPassword=$5

# this empty '' in below sed commands is applicable only for macOS, remove it in linux
sed -i '' "s|<APP-BASE-URL>|$appBaseUrl|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<KEYCLOAK-INTERNAL-BASE-URL>|$keycloakInternalBaseUrl|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<KEYCLOAK-EXTERNAL-BASE-URL>|$keycloakExternalBaseUrl|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<URL-SHORTENER-CLIENT-SECRET>|$urlshortenerClientSecret|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<GMAIL-APP-PASSWORD>|$gmailAppPassword|g" ./realm/urlshortener-keycloak-realm.json

exit 0
