FROM node:16-alpine

WORKDIR /app
COPY package*.json ./
RUN npm install react-scripts -g --silent
COPY . /app
RUN pwd
RUN ls
RUN npm run build

ENTRYPOINT ["npx", "serve", "-s", "build"]
EXPOSE 3000