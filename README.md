# Digital Money - Virtual Wallet

Este es el proyecto final de **Digital House**, donde se ha implementado una billetera virtual llamada **Digital Money**. El proyecto está compuesto por un frontend desarrollado en **React** con **TypeScript** y un backend basado en **microservicios** utilizando **Spring Boot**. La aplicación permite gestionar usuarios, cuentas, tarjetas y actividades financieras.

## Tabla de contenidos

- [Descripción General](#descripción-general)
- [Microservicios](#microservicios)
- [Requisitos Previos](#requisitos-previos)
- [Instrucciones de Uso](#instrucciones-de-uso)
  - [Levantar el Frontend](#levantar-el-frontend)
  - [Levantar el Backend](#levantar-el-backend)
  - [Levantar Redis con Docker](#levantar-redis-con-docker)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)

## Descripción General

**Digital Money** es una billetera virtual que permite a los usuarios realizar una variedad de operaciones financieras como la creación de cuentas, gestión de tarjetas, y monitoreo de actividades financieras. El proyecto sigue una arquitectura de microservicios, lo que facilita la escalabilidad y el mantenimiento de cada uno de los componentes.

## Microservicios

El backend está compuesto por los siguientes microservicios:

- **Eureka**: Servicio de descubrimiento para los demás microservicios.
- **Config-server**: Servicio que gestiona la configuración centralizada de todos los microservicios.
- **Gateway**: Enrutador API para gestionar el tráfico hacia los microservicios.
- **Auth-server**: Servicio encargado de la autenticación y generación de tokens JWT.
- **Accounts-server**: Servicio que gestiona las cuentas bancarias.
- **Users-server**: Servicio que maneja la gestión de usuarios.
- **Activities-server**: Servicio que registra las actividades financieras de los usuarios.
- **Cards-server**: Servicio que gestiona las tarjetas de crédito y débito.
- **Redis (en Docker)**: Utilizado para almacenar tokens JWT inválidos, ayudando a la gestión de sesiones y seguridad.

## Requisitos Previos

Para ejecutar este proyecto, necesitarás lo siguiente:

- **Node.js** (para el frontend)
- **NPM** (para la gestión de dependencias en el frontend)
- **JDK 17** (para el backend)
- **Docker** (para correr Redis)

## Instrucciones de Uso

### Levantar el Frontend

1. Clona el repositorio del frontend en tu máquina local.
2. Entra en el directorio del frontend:
   ```bash
   cd frontend
   ```
3. Instala las dependencias:
  ```bash
  npm install
  ```
4. Ejecuta la aplicación:
  ```bash
  npm start
  ```
### Levantar el Backend

1. Asegúrate de tener configurado tu entorno de desarrollo para Java y Spring Boot (IDE como **IntelliJ** o **Eclipse**).
2. Clona el repositorio del backend en tu máquina local.
3. Importa cada uno de los microservicios como proyectos de Spring Boot en tu IDE.
4. Levanta cada microservicio desde el IDE:
   - **Eureka** debe levantarse primero, seguido por **Config-server**.
   - El resto de los microservicios deben levantarse después.

### Levantar Redis con Docker

Redis se utiliza para almacenar tokens JWT inválidos. Para levantar Redis, asegúrate de tener Docker instalado y ejecuta el siguiente comando:

```bash
docker run --name redis -p 6379:6379 -d redis
```
## Tecnologías Utilizadas

- **Frontend**: React, TypeScript, NPM
- **Backend**: Spring Boot, Spring Cloud, Java 17, Eureka, Feign, Config-server
- **Base de Datos**: Redis (para tokens JWT), otras bases de datos dependiendo del microservicio
- **Contenedores**: Docker (para Redis)
