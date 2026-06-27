FROM bellsoft/liberica-openjdk-debian:21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true
COPY src src
RUN ./gradlew build -x test --no-daemon

FROM bellsoft/liberica-openjdk-debian:21
WORKDIR /app
COPY --from=build /app/build/libs/app.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m", "-XX:+UseZGC", "-jar", "app.jar"]