FROM openjdk:17-jdk-slim
WORKDIR /app
COPY  build/libs/bank-0.0.1-SNAPSHOT.jar app.jar
RUN chmod +x /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]