FROM mysql:9.1.0

#todo will the database be exposed without below line?
EXPOSE 3306/tcp

ADD ./mysqlusers.sql /docker-entrypoint-initdb.d/

