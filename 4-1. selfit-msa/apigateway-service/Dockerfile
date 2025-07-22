FROM openjdk:21-jdk

COPY target/apigateway-service-1.0.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app.jar"]
