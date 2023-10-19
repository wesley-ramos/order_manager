FROM maven:3.5-jdk-8-alpine as builder

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

ADD . /usr/src/app

RUN mvn package -Dmaven.test.skip=true


FROM openjdk:8-jdk-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY --from=builder /usr/src/app/target/order_manager.jar app.jar

ENTRYPOINT java -XX:MaxMetaspaceSize=256m  -Xmx256m -jar app.jar