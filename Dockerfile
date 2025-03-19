# Use official OpenJDK 11 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged WAR file into the container
COPY target/interview-service-0.0.1-SNAPSHOT.jar interview-service-0.0.1-SNAPSHOT.jar

# Expose the port the application runs on
EXPOSE 8082

# Specify the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "interview-service-0.0.1-SNAPSHOT.jar"]