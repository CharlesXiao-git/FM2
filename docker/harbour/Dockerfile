# Docker multi-stage build

# 1. Building the App with Maven
FROM maven:3-jdk-11

ADD . /harbour
WORKDIR /harbour

# Just echo so we can see, if everything is there :)
RUN ls -l

# Run Maven build
RUN mvn -Dsonar.login=$SONAR_TOKEN clean install

# Just using the build artifact and then removing the build-container
FROM openjdk:11-jdk

MAINTAINER James Boyce

# Add Spring Boot app.jar to Container
COPY --from=0 "/harbour/backend/target/backend-0.0.1-SNAPSHOT.jar" app.jar

ENV JAVA_OPTS=""

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]