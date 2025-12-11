FROM amazoncorretto:21-alpine

WORKDIR /app

COPY target/urlshortener-app-1.0.0.jar urlshortener-app-1.0.0.jar

EXPOSE 8080/tcp

CMD ["java", "-jar", "urlshortener-app-1.0.0.jar", "--spring.profiles.active=k8s"]

