# Use a imagem oficial do Java 17
FROM openjdk:17-jdk-slim

# Cria um diretório dentro do container
WORKDIR /app

# Copia o .jar da pasta target para dentro do container
COPY target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Roda o jar
ENTRYPOINT ["java", "-jar", "app.jar"]