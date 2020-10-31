FROM node:12.4.0-alpine as frontend
# WORKDIR /frontend
# COPY frontend .
# RUN npm ci
# RUN npm install
# RUN npm run-script build

# FROM maven:3.6.3-jdk-14 as backend
# WORKDIR /backend
# COPY backend .
# RUN mkdir -p src/main/resources/static
# COPY --from=frontend /frontend/build src/main/resources/static
# RUN mvn clean verify

# FROM openjdk:14-jdk-alpine
# COPY --from=backend /backend/target/*.jar ./app.jar
# COPY --from=backend /backend/src/main/resources/tmp_keystore.jks .//src/main/resources/tmp_keystore.jks
# CMD [ "sh", "-c", "java -Dserver.port=$PORT -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]

# set working directory
# WORKDIR /frontend
COPY . ./

# install app dependencies
COPY package.json ./
COPY package-lock.json ./
RUN npm ci
RUN npm install
RUN npm run-script build

# add app
COPY . ./

# start app
RUN npm install -g serve
CMD [ "serve", "-s", "build" ]
