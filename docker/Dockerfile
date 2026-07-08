FROM openjdk:25-jdk-slim

WORKDIR /app

ARG APP_VERSION=0.0.1
COPY kot-server-${APP_VERSION}.zip /app/
RUN unzip kot-server-${APP_VERSION}.zip && \
    rm kot-server-${APP_VERSION}.zip

EXPOSE 8080

ENTRYPOINT ["/app/kot-server-0.0.1/bin/kot-server"]
