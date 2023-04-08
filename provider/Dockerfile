FROM node:16-alpine

WORKDIR /app

COPY package*.json ./

RUN npm config set cache /node_cache \
    && npm install --cache /node_cache \
    && rm -rf /node_cache/* \
    && chown -R node:node /app /node_cache

COPY . .

RUN npm run build

CMD ["npm", "install", "-g", "serve"]

CMD ["serve", "-s", "build"]

EXPOSE 3000