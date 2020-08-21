# Docker multi-stage build

# 1. Building the App with Maven
FROM maven:3-jdk-11

ADD . /harbour/backend/target
WORKDIR /harbour/backend/target

# Just echo so we can see, if everything is there :)
RUN ls -l

# Just using the build artifact and then removing the build-container
FROM openjdk:11-jdk

MAINTAINER James Boyce

# Add Spring Boot app.jar to Container
COPY --from=0 "/harbour/backend/target/backend*.jar" app.jar

ENV JAVA_OPTS=""

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]