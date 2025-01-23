FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/your-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]