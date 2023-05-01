FROM openjdk:17-alpine
ARG FILEPATH=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]