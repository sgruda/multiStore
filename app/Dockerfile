FROM node:12.4.0-alpine as frontend
WORKDIR /frontend
COPY frontend .
RUN npm ci
RUN npm run-script build

FROM maven:3.6.3-jdk-14 as backend
WORKDIR /backend
COPY backend .
RUN mkdir -p src/main/resources/static
COPY --from=frontend /frontend/build src/main/resources/static
RUN mvn clean verify

FROM openjdk:14-jdk-alpine
COPY --from=backend /backend/target/*.jar ./app.jar
# EXPOSE 8080
# EXPOSE $PORT
# RUN adduser -D user
# USER user
CMD [ "sh", "-c", "java -Dserver.port=$PORT -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]