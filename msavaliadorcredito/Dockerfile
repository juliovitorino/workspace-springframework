FROM maven:3.8.1-jdk-11 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11.0.9
WORKDIR /app
COPY --from=build ./build/target/*.jar ./app.jar

ARG RABBITMQ_SERVER=rabbitmq-server-container
ARG RABBITMQ_SERVER_PORT=5672
ARG EUREKA_SERVER=discovery-eureka-server-container
ARG EUREKA_SERVER_PORT=8761

ENTRYPOINT ["java", "-jar", "app.jar"]
