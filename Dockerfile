FROM maven:3.9-amazoncorretto-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.21

ENV db_url="jdbc:postgresql://host.docker.internal:5432/products_api"
ENV password="vinicius"
ENV user="postgres"
ENV aws.bucketName="api-spring-produto"

COPY --from=build app/target/products-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 5432
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]