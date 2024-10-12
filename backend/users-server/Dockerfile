# Dockerfile for Users Server
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/users-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
