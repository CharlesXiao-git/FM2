# Download the build artifact and add to this container
FROM openjdk:11-jdk

ADD . /harbour
WORKDIR /harbour

# Just echo so we can see, if everything is there :)
RUN ls -l

MAINTAINER James Boyce

# Add Spring Boot app.jar to Container
COPY --from=0 "backend*.jar" app.jar

ENV JAVA_OPTS=""

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]