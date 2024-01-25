FROM gradle:8.5-jdk21 AS build
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN gradle build

FROM openjdk:21
COPY --from=build /app/build /app/build
EXPOSE 8080
ENTRYPOINT ["java","-jar","app/build/libs/bookshelf-0.0.1.jar"]
