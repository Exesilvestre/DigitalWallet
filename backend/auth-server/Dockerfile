# Dockerfile for Auth Server
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/auth-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
