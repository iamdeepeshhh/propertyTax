# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Install necessary packages
RUN apk update && apk add --no-cache freetype

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's JAR file
ARG JAR_FILE=target/3GAssociates-0.0.1-SNAPSHOT.jar

# Add the application's JAR file to the container
COPY ${JAR_FILE} app.jar

# Run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]