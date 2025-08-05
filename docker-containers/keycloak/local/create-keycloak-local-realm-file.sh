#!/bin/zsh

./../realm-template/replace-realm-vars.sh \
--app-external-base-url "http://localhost:8081" \
--app-internal-base-url "http://localhost:8081" \
--keycloak-external-base-url "http://localhost:8080" \
--keycloak-internal-base-url "http://localhost:8080" \
--urlshortener-client-secret "qQUimZxrmnop3ug6UqctmCKgkqPVIhV9" \
--gmail-app-password "yuvncagklxranszg"
exit 0
