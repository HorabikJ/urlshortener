#!/bin/bash

mkdir -p realm

./../realm-template/replace-realm-vars.sh \
--app-external-base-url "http://13.51.249.172" \
--app-internal-base-url "http://172.31.11.141" \
--keycloak-external-base-url "http://16.16.255.34" \
--keycloak-internal-base-url "http://172.31.14.104" \
--urlshortener-client-secret "qQUimZxrmnop3ug6UqctmCKgkqPVIhV9" \
--gmail-app-password "yuvncagklxranszg"
exit 0
