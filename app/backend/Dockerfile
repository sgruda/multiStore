
FROM maven:3.6.3-jdk-14 as backend

COPY . ./
RUN mvn clean verify

FROM openjdk:14-jdk-alpine
COPY --from=backend /target/*.jar ./app.jar
CMD [ "sh", "-c", "java -Dserver.port=$PORT -jar app.jar" ]