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
# Use proper hostname instead of `localhost`, like ip adress when deployed in production/aws
"--hostname" , "13.61.1.70", \
# With the below set to true, Keycloak will only accept requests where the Host header matches `hostname` value from 
# above. When it is false, Keycloak will not check the Host header in the request.
"--hostname-strict", "false", \
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
"--db-url-host", "172.31.23.21", \
"--db-schema", "keycloak", \
"--db-url-port", "3306" \
]


