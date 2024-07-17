FROM maven:3.8.6-openjdk-18-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml -DskipTests=true clean package

# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /home/app/target/url_shortner.jar /app/url_shortner.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/url_shortner.jar"]