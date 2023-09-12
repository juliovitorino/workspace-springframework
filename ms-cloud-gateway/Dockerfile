FROM maven:3.8.1-jdk-11 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11.0.9
WORKDIR /app
COPY --from=build ./build/target/*.jar ./app.jar

ARG EUREKA_SERVER=discovery-eureka-server-container

ENTRYPOINT ["java", "-jar", "app.jar"]
