FROM maven:3.8.1-jdk-11 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11.0.9

WORKDIR /app
COPY --from=build ./build/target/ms-eureka-server.jar ./app.jar

ARG EUREKA_SERVER_PORT=8761

ENTRYPOINT ["java", "-jar", "app.jar"]
