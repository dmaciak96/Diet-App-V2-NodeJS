FROM openjdk:22-jdk-slim AS runner
WORKDIR /diet-app
COPY ./target/diet-app-*.jar ./diet-app.jar
COPY ./src/main/resources/application.yml ./application.yml
CMD ["java", "-jar", "./diet-app.jar"]