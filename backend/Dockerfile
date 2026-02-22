FROM eclipse-temurin:25-jre AS build
WORKDIR /app

# Copy the pre-built JAR from the target directory
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080
