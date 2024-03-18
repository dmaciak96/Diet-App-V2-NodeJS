FROM openjdk:22-jdk-slim AS runner
WORKDIR /webdiet
COPY ./target/webdiet-*.jar ./webdiet.jar
COPY ./src/main/resources/application.yml ./application.yml
CMD ["java", "-jar", "./webdiet.jar"]