FROM openjdk:8-jdk-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

ADD target/order_manager.jar app.jar

ENTRYPOINT java -XX:MaxMetaspaceSize=256m  -Xmx256m -jar app.jar