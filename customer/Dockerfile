FROM node:16-alpine

WORKDIR /app

COPY package*.json yarn.lock ./

RUN yarn install --modules-folder /node_modules

COPY . .

RUN yarn run build

CMD ["yarn", "global", "add", "serve"]

CMD ["serve", "-s", "build"]

EXPOSE 3000

VOLUME /node_modules