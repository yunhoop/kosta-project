FROM openjdk:22-jdk

COPY target/discovery-service-1.0.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app.jar"]
