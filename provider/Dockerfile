FROM node:16-alpine

WORKDIR /app

COPY package*.json yarn.lock ./

RUN yarn install

COPY . .

CMD ["yarn", "global", "add", "serve"]

RUN yarn run build

CMD ["serve", "-s", "build"]
EXPOSE 3000