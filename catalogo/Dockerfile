FROM azul/zulu-openjdk:24

WORKDIR /app

COPY target/catalogo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]