# Usando la imagen oficial de Azul Zulu OpenJDK 24
FROM azul/zulu-openjdk:24

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR construido (asegúrate que el nombre coincide con tu pom.xml)
COPY target/catalogo-0.0.1-SNAPSHOT.jar app.jar

# Puerto expuesto (opcional, buena práctica documentar)
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]