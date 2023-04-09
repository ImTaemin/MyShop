FROM openjdk:11-jre-slim
COPY target/*.jar /api.jar
RUN chmod 755 /api.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/api.jar"]
EXPOSE 8080