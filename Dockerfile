FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y python3 python3-pip
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY .env .env
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","/app.jar"]
