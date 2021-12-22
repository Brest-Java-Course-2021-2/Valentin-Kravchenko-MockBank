FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /src
COPY target/web-app-exec.jar src/web-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "src/web-app.jar"]