FROM azul/zulu-openjdk:24

WORKDIR /app

COPY target/inventory-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]