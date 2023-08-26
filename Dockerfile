# Use an official OpenJDK runtime as a parent image
FROM openjdk:17
# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot JAR file into the container at /app
COPY target/foodspring-1.0.0.jar /app/app.jar

# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]
