FROM node:16-alpine

WORKDIR /app
COPY package.json .
COPY yarn.lock .
RUN yarn install --frozen-lockfile
COPY . .
RUN yarn build

CMD ["serve", "-s", "build"]
EXPOSE 3000