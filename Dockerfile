FROM maven:3.8.5-openjdk-17 as build

WORKDIR /app
COPY . /app/.

RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/*.jar /app/*.jar

ENTRYPOINT ["java", "-jar", "/app/*.jar"]