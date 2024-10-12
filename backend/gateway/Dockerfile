# Dockerfile for Gateway
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/gateway-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
