FROM node:16-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

CMD ["npm", "install", "-g", "serve"]

CMD ["serve", "-s", "build"]

EXPOSE 3000

VOLUME /node_modules