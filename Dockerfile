FROM openjdk:17-slim

ENV TZ=Asia/Seoul
EXPOSE 8080

ARG JAR_FILE=build/libs/CertisHomepage-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]


#
#FROM openjdk:17-slim
#
## Set the time zone
#ENV TZ=Asia/Seoul
#RUN apt-get update && apt-get install -y tzdata \
#    && ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
#    && echo $TZ > /etc/timezone \
#    && apt-get clean \
#    && rm -rf /var/lib/apt/lists/*
#
## Expose port 8080 for the Spring Boot application
#EXPOSE 20000
#
## Define the argument for the JAR file location
#ARG JAR_FILE
#
## Copy the JAR file into the container
#COPY ${JAR_FILE} app.jar
#
## Command to run the Spring Boot application
#ENTRYPOINT ["java", "-jar", "/app.jar"]