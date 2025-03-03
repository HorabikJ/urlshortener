# using below version as in newer version exporting realm via docker exec command does not work
FROM quay.io/keycloak/keycloak:25.0.1

EXPOSE 8080/tcp

ENV KEYCLOAK_ADMIN="admin"
ENV KEYCLOAK_ADMIN_PASSWORD="admin"
ENV KC_HTTP_PORT="8080"
ENV KC_HOSTNAME_URL="http://localhost:8080"
ENV KC_HOSTNAME_ADMIN_URL="http://localhost:8080"
ENV KC_HOSTNAME_STRICT_BACKCHANNEL="true"
#ENV KC_HOSTNAME_DEBUG="true"
ENV KC_HTTP_RELATIVE_PATH="/"
ENV KC_HTTP_ENABLED="true"
ENV KC_HEALTH_ENABLED="true"
ENV KC_METRICS_ENABLED="true"
#ENV KC_LOG_LEVEL="DEBUG"

ADD ./realm/urlshortener-keycloak-realm.json /opt/keycloak/data/import/

#todo do not start keycloak dev in production
#todo encrypt password in line ""password": "yuvncagklxranszg"," in realm file
CMD ["start-dev", "--import-realm"]

