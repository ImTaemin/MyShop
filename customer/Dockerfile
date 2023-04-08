FROM node:16-alpine

WORKDIR /app

COPY package*.json .
CMD ["yarn", "global", "add", "serve"]

COPY . .
RUN yarn run build

CMD ["serve", "-s", "build"]
EXPOSE 3000