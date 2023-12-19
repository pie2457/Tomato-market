# server base image - java 11
FROM adoptopenjdk/openjdk11

# copy .jar file to docker
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar

# always do command
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
