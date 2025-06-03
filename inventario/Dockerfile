FROM azul/zulu-openjdk:24

WORKDIR /app

COPY target/inventario-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]