# using below version as in newer version exporting realm via docker exec command does not work
FROM quay.io/keycloak/keycloak:25.0.1

EXPOSE 80/tcp

ENV KEYCLOAK_ADMIN="admin"
ENV KEYCLOAK_ADMIN_PASSWORD="admin"

ADD realm/urlshortener-keycloak-realm.json /opt/keycloak/data/import/

CMD [ "start", \
"--verbose", \
"--features", "hostname:v2", \
"--http-port", "80", \
# Use proper hostname instead of "localhost", like ip adress when deployed in production/aws
"--hostname" , "localhost", \
# remove below line in production/aws
"--hostname-debug", "true", \
"--http-relative-path", "/", \
"--http-enabled", "true", \
"--health-enabled", "true", \
"--metrics-enabled", "true", \
"--import-realm", \
"--db", "mysql", \
"--db-username", "keycloak-admin", \
"--db-password", "password", \
"--db-url-host", "<configure>", \
"--db-schema", "keycloak", \
"--db-url-port", "3306" \
]


