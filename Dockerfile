FROM maven:3.8.3-openjdk-17 AS build
RUN mkdir /buildApp
COPY . /buildApp
WORKDIR /buildApp
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
RUN mkdir /app
COPY --from=build /buildApp/target/exchange-0.0.1-SNAPSHOT.jar /app/exchange-application.jar
WORKDIR /app
CMD "java" "-jar" "exchange-application.jar"