# Start with a base image containing Java runtime and Maven
FROM maven:latest as build

# Make source folder
RUN mkdir -p /build
WORKDIR /build

# Copy your pom.xml and source code to the docker image
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Start with a base image containing Java runtime
FROM openjdk:latest

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Copy the application's jar file from the build image to this new image
COPY --from=build /build/target/socialse2.jar socialse2.jar

# Run the jar file 
ENTRYPOINT ["java","-jar","/socialse2.jar"]
