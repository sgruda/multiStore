# # Docker Image which is used as foundation to create
# # a custom Docker Image with this Dockerfile
# FROM node:10
 
# # A directory within the virtualized Docker environment
# # Becomes more relevant when using Docker Compose later
# WORKDIR /usr/src/app
 
# # Copies package.json and package-lock.json to Docker environment
# COPY package*.json ./
 
# # Installs all node packages
# RUN npm install
 
# # Copies everything over to Docker environment
# COPY . .
 
# # Uses port which is used by the actual application
# EXPOSE 3000
 
# # Finally runs the application
# CMD [ "npm", "start" ]




# #### Stage 1: Build the react application
# FROM node:12.4.0-alpine as build

# # Configure the main working directory inside the docker image. 
# # This is the base directory used in any further RUN, COPY, and ENTRYPOINT 
# # commands.
# WORKDIR /app

# # Copy the package.json as well as the package-lock.json and install 
# # the dependencies. This is a separate step so the dependencies 
# # will be cached unless changes to one of those two files 
# # are made.
# COPY package.json package-lock.json ./
# RUN npm install

# # Copy the main application
# COPY . ./

# # Arguments
# ARG REACT_APP_API_BASE_URL
# ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}

# # Build the application
# RUN npm run build

# #### Stage 2: Serve the React application from Nginx 
# FROM nginx:1.17.0-alpine

# # Copy the react build from Stage 1
# COPY --from=build /app/build /var/www

# # Copy our custom nginx config
# COPY nginx.conf /etc/nginx/nginx.conf

# # Expose port 80 to the Docker host, so we can access it 
# # from the outside.
# EXPOSE 80

# ENTRYPOINT ["nginx","-g","daemon off;"]
FROM node:12.4.0-alpine as build

COPY . /usr/src/app
WORKDIR /usr/src/app

CMD ["npm", "start"]