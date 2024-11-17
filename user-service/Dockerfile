FROM openjdk:23-jdk-alpine
WORKDIR /app
COPY target/user-service.jar user-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user-service.jar"]