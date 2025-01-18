# Use a imagem oficial do Maven para construir a aplicação
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app

# Copie o arquivo pom.xml e as dependências do Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Copie o código-fonte da aplicação
COPY src ./src

# Compile a aplicação
RUN mvn clean package -DskipTests

# Use uma imagem do Amazon Corretto 21 para rodar a aplicação
FROM amazoncorretto:21
WORKDIR /app

# Copie o JAR da aplicação do estágio de build
COPY --from=build /app/target/NearbyAgenciesApiApplication-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que a aplicação irá rodar
EXPOSE ${PORT}

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]