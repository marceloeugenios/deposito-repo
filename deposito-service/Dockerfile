FROM openjdk:11-jre-slim-buster

ARG JAR_FILE=deposito-service-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} deposito.jar

ENTRYPOINT ["java","-jar","deposito.jar"]

EXPOSE 8080