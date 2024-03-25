FROM eclipse-temurin:17-jdk-alpine
COPY ./target/email-notifier-0.0.1.jar app.jar
ENV CONFIG_SERVER_HOST=config-server
ENV EMAIL_PROVIDER_HOST=sensor-emails-provider
ENTRYPOINT [ "java", "-jar", "app.jar" ]