# Dockerfile for Activities Server
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/activities-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]