# using below version as in newer version exporting realm via docker exec command does not work
FROM quay.io/keycloak/keycloak:25.0.1

EXPOSE 8080/tcp

ENV KEYCLOAK_ADMIN="admin"
ENV KEYCLOAK_ADMIN_PASSWORD="admin"
#ENV KEYCLOAK_FRONTEND_URL="http://urlshortener.keycloak.com/auth"
#ENV PROXY_ADDRESS_FORWARDING="true"

ADD ./realm/urlshortener-keycloak-realm.json /opt/keycloak/data/import/

#todo do not start keycloak dev in production
#todo encrypt password in line ""password": "yuvncagklxranszg"," in realm file
CMD [ "start", \
#"start-dev", \
"--verbose", \
"--features", "hostname:v2", \
"--http-port", "8080", \
"--hostname" , "localhost", \
#"--hostname-admin", "localhost", as per `./kc.sh start --help` command I am not setting this
"--hostname-debug", "true", \
"--http-relative-path", "/", \
"--http-enabled", "true", \
# If the server should expose health check endpoints. If enabled, health checks are available at the '/health', '/health/ready' and '/health/live' DEPRECATED. If metrics/health endpoints should be exposed on the main HTTP
"--health-enabled", "true", \
# If the server should expose metrics. If enabled, metrics are available at the '/metrics' endpoint. Default: false.
"--metrics-enabled", "true", \
"--import-realm", \
"--db", "mysql", \
"--db-username", "keycloak-admin", \
"--db-password", "password", \
"--db-url-host", "urlshortener-db-service.urlshortener-dev", \
"--db-schema", "keycloak", \
"--db-url-port", "3306" \
]


