FROM maven:3.8.5-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src /app/src
RUN mvn clean install

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/target/lovelink-back-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]