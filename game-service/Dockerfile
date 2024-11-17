FROM openjdk:23-jdk-alpine
WORKDIR /app
COPY target/game-service.jar game-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "game-service.jar"]