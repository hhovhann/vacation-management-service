FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:21-slim-bullseye
WORKDIR /usr/src/app

RUN mkdir -p /usr/src/app/.m2 && mkdir /usr/src/app/.m2/repository
COPY --from=build /usr/src/app/target/vacation-management-service-0.0.7-SNAPSHOT.jar .

EXPOSE 8080
ENTRYPOINT ["java","-jar","vacation-management-service-0.0.7-SNAPSHOT.jar"]