# Step 1: Use OpenJDK 17 image for building the application
FROM openjdk:17-jdk-slim AS build

# Set the working directory inside the container
WORKDIR /custom-web-crawler

# Copy the Gradle wrapper and build files
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Copy the source code to the container
COPY src/ src/

# Give execution permission to gradlew
RUN chmod +x gradlew

# Build the Spring Boot application JAR (without running tests)
RUN ./gradlew clean build -x test

# Step 2: Create the final image based on a smaller JDK image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /

# Copy the JAR file from the previous step into the container
COPY --from=build /custom-web-crawler/build/libs/custom-web-crawler-0.0.1-SNAPSHOT.jar /app/

# Expose the Spring Boot application's port
EXPOSE 8080

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/custom-web-crawler-0.0.1-SNAPSHOT.jar"]