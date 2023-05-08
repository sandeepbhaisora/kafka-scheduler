FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . /app
RUN pwd && ls -la
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/kafka-scheduler-0.0.1-SNAPSHOT.jar"]
