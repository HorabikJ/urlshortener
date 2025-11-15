#!/bin/bash

mkdir -p realm

./../realm-template/replace-realm-vars.sh \
--app-external-base-url "http://56.228.16.205" \
--app-internal-base-url "http://172.31.21.177" \
--keycloak-external-base-url "http://13.61.1.70" \
--keycloak-internal-base-url "http://172.31.19.242" \
--urlshortener-client-secret "qQUimZxrmnop3ug6UqctmCKgkqPVIhV9" \
--gmail-app-password "yuvncagklxranszg"
exit 0
