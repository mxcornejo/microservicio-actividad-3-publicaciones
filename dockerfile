# Partimos de una imagen de Java 17 con Alpine (más ligera)
FROM openjdk:17-jdk-slim 
 
 
# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app
 
 
# Establecemos el directorio donde se montará la wallet de Oracle dentro del contenedor
ENV ORACLE_WALLET_DIR=/app/Wallet
 
 
# Crea un directorio en el contenedor para la wallet
RUN mkdir -p $ORACLE_WALLET_DIR
 
 
# Copia los archivos de la wallet (tnsnames.ora, sqlnet.ora, etc.) al contenedor
COPY Wallet/ $ORACLE_WALLET_DIR/
 
 
# Copiamos el JAR generado en el contenedor
COPY target/publicaciones-0.0.1-SNAPSHOT.jar app.jar
# Exponemos el puerto 8080 (el que usa Spring Boot por defecto)
EXPOSE 8080
# Comando para ejecutar la aplicación cuando el contenedor arranque
ENTRYPOINT ["java", "-jar", "app.jar"]