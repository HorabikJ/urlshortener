FROM keycloak/keycloak:26.1

EXPOSE 8080/tcp

ENV KC_BOOTSTRAP_ADMIN_USERNAME="admin"
ENV KC_BOOTSTRAP_ADMIN_PASSWORD="admin"
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

ADD ./realm/baeldung-keycloak-realm.json /opt/keycloak/data/import/

#todo do not start keycloak dev in production
CMD ["start-dev", "--import-realm"]

