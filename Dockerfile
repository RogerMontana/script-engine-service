FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S scriptengine && adduser -S scriptengine -G scriptengine
USER scriptengine:scriptengine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]