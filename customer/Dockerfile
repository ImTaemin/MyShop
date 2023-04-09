FROM nginx

WORKDIR /app

# /app/build 폴더 생성
RUN mkdir ./build

# host의 빌드된 폴더를 /app/build에 복사
ADD ./build ./build

RUN rm /etc/nginx/conf.d/default.conf

# host의 nginx 설정 파일 복사
COPY ./customer.conf /etc/nginx/conf.d

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]