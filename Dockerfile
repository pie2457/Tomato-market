# server base image - java 11
FROM adoptopenjdk/openjdk11

# copy .jar file to docker
COPY build/libs/tomato-market-0.0.1-SNAPSHOT.jar app.jar

# always do command
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
