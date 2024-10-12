# Dockerfile for Cards Server
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/cards-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
