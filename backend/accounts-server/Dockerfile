FROM openjdk:17-jdk-alpine

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado por el build de tu servicio
COPY target/accounts-server-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto que va a usar el microservicio (en este caso, 8085)
EXPOSE 8085

# Comando para ejecutar el JAR del microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]