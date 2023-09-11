FROM openjdk:11.0.9
EXPOSE 8761
WORKDIR /app
COPY ./target/ms-eureka-server.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
