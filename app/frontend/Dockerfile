FROM node:12.4.0-alpine as frontend

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
