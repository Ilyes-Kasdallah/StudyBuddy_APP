FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/calendar-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
