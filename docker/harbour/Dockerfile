# Download the build artifact and add to this container
FROM openjdk:11-jdk

MAINTAINER Terry Sposato <terry@freightmate.com>

# Add Spring Boot app.jar to Container
COPY "backend*.jar" app.jar

ENV JAVA_OPTS=""

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]