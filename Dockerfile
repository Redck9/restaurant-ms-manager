FROM openjdk:17
COPY build/libs/*.jar redck-restaurant-ms-manager.jar
COPY src/main/resources/compose.yaml .
ENTRYPOINT ["java", "-jar", "redck-restaurant-ms-manager.jar"]
EXPOSE 8181
