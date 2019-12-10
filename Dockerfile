FROM openjdk:11.0.3-jdk as builder

WORKDIR /
COPY . .
RUN ./mvnw clean install

FROM openjdk:11.0.3-jdk
VOLUME /tmp
COPY --from=builder target/app.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]