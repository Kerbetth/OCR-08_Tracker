FROM gradle as builder
RUN gradle build

FROM openjdk:14

ADD build/libs/tourguide-tracker-1.0.0.jar tourguide-tracker-1.0.0.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "tourguide-tracker-1.0.0.jar"]
