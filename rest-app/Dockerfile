FROM adoptopenjdk/openjdk11:alpine-jre
RUN apk --no-cache add curl
WORKDIR /src
COPY target/rest-app-exec.jar src/rest-app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "src/rest-app.jar"]