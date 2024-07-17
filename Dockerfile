# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:17

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY ./target/url_shortner.jar /app/url_shortner.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run on container startup
ENTRYPOINT ["java","-jar","/app/url_shortner.jar"]