FROM maven:3.9.9-amazoncorretto-21-alpine

WORKDIR /code

ADD pom.xml /code/pom.xml
ADD src/main/java /code/src/main/java
RUN ["mvn", "dependency:resolve"]

#todo below line is not needed
#EXPOSE 8080/tcp

CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=k8s"]

