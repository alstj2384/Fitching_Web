# Base image
FROM openjdk:17-slim

# Install required packages
RUN apt-get update && apt-get install -y build-essential curl
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x ./wait-for-it.sh

# Add wait-for-it script

# Set JAVA_HOME environment variable
ENV JAVA_HOME /usr/local/openjdk-17
ENV PATH $JAVA_HOME/bin:$PATH

# Set the working directory
WORKDIR /app

# Copy the project files to the working directory
COPY . .

# Ensure Gradle wrapper is executable
RUN chmod +x ./gradlew

# Build the application using Gradle, skipping tests
RUN ./gradlew build -x test

# Verify that the jar file exists
RUN ls -al /app/build/libs/

# Run the built JAR file, waiting for MariaDB to be available
CMD ["sh", "-c", "/wait-for-it.sh db:3306 -- java -jar build/libs/fitchingWeb-0.0.1-SNAPSHOT.jar"]
