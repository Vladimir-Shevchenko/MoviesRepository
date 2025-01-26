# Stage 1: Build the application
FROM maven:3.8.5-openjdk-11 AS build

WORKDIR /app

# Copy the pom.xml and fetch dependencies
COPY pom.xml /app/
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src /app/src
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/demo.jar"]











## Stage 1: Build the application
#FROM openjdk:11-jdk-slim AS build
#
## Install Maven
#RUN apt-get update && apt-get install -y maven
#
## Set working directory
#WORKDIR /app
#
## Copy the pom.xml and download the dependencies
#COPY pom.xml .
#RUN mvn dependency:go-offline -B

## Copy the source code


#COPY src /app/src
#
## Build the application
#RUN mvn clean install -DskipTests
#
## Stage 2: Run the application
#FROM openjdk:11-jre-slim
#
#WORKDIR /app
#
## Copy the jar file from the build stage
#COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar
#
## Run the application
#ENTRYPOINT ["java", "-jar", "/app/demo.jar"]
#
#EXPOSE 8080
