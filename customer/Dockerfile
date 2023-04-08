FROM node:16-alpine

WORKDIR /app

COPY package*.json .
RUN yarn install -g serve

COPY . .
RUN yarn run build

CMD ["serve", "-s", "build"]
EXPOSE 3000