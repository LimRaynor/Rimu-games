FROM gradle:jdk21-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/build/libs/*.jar ./
RUN mv $(ls *.jar | grep -v plain) app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
