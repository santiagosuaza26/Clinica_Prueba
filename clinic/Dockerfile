# Usar una imagen base de OpenJDK 17 con Maven incluido
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Imagen de runtime más ligera
FROM eclipse-temurin:17-jre-alpine

# Instalar curl para healthchecks
RUN apk add --no-cache curl

# Crear usuario no root para seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR construido desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar propietario de los archivos
RUN chown -R appuser:appgroup /app

# Cambiar a usuario no root
USER appuser

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación con configuración optimizada
CMD ["java", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-jar", \
     "app.jar"]