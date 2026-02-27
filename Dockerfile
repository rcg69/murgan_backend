# Use an official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY target/ecommerce-backend-0.0.1-SNAPSHOT.jar ecommerce-backend.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "ecommerce-backend.jar"]
