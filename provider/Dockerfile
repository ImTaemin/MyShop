FROM node:16-alpine

WORKDIR /app
RUN yarn global add serve@14.2.0

COPY package*.json ./
RUN yarn install --silent --frozen-lockfile

COPY . /app
RUN yarn run build

CMD ["serve", "-s", "build"]
EXPOSE 3000