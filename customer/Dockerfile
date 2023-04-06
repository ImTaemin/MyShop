FROM node:16-alpine

WORKDIR /app
COPY package*.json yarn.lock ./
RUN yarn install --silent
COPY . /app
RUN yarn run build

ENTRYPOINT ["npx", "serve", "-s", "build"]
EXPOSE 3000