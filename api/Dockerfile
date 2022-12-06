FROM openjdk:11-jre-slim
COPY target/*.jar /provider.jar
RUN chmod 755 /provider.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/provider.jar"]
EXPOSE 8080