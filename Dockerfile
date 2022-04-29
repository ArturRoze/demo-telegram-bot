FROM openjdk:8

COPY target/demo-telegram-bot.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8082
