#!/bin/zsh

./replace-realm-vars.sh \
--app-external-base-url "http://localhost:30008" \
--app-internal-base-url "http://urlshortener-backend-service.urlshortener-dev:8080" \
--keycloak-external-base-url "http://localhost:30009" \
--keycloak-internal-base-url "http://urlshortener-keycloak-service.urlshortener-dev:8080" \
--urlshortener-client-secret "qQUimZxrmnop3ug6UqctmCKgkqPVIhV9" \
--gmail-app-password "yuvncagklxranszg"
exit 0
