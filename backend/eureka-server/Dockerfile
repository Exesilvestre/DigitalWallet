# Dockerfile for Eureka Server
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/eureka-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
