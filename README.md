# Nearby Agency API 🏦

## Visão Geral do Projeto 🌐
- **Título do Projeto:** nearagencyapi
- **Tecnologias Utilizadas:** Spring Framework, Java 21
- **Arquitetura:** Arquitetura Limpa
- **Princípios:** SOLID
- **Tipo de API:** REST
- **Banco de Dados:** NoSQL (Amazon DynamoDB)

## Decisões Técnicas 🛠️

### 1. Linguagem e Framework 💻
- **Java 21:** Escolhida por sua robustez e grande comunidade de suporte.
- **Spring Framework:** Utilizado por sua eficiência na criação de aplicações empresariais e suporte a boas práticas de desenvolvimento.

### 2. Arquitetura 🏛️
- **Arquitetura Limpa:** Adotada para garantir a separação de responsabilidades e facilitar a manutenção e evolução do código.

### 3. Princípios SOLID 📐
- **Single Responsibility Principle:** Cada classe tem uma única responsabilidade ou motivo para mudar.
- **Open/Closed Principle:** Entidades de software devem estar abertas para extensão, mas fechadas para modificação.
- **Liskov Substitution Principle:** Subtipos devem ser substituíveis por seus tipos base sem alterar o comportamento do programa.
- **Interface Segregation Principle:** Interfaces mais específicas são melhores do que interfaces gerais.
- **Dependency Inversion Principle:** Módulos de alto nível não devem depender de módulos de baixo nível, mas sim de abstrações.

## Decisões Arquiteturais 🏗️

### 1. Padrões de Projeto 🧩
- **Ainda não aplicados:** A implementação de padrões será avaliada conforme o projeto evolui.

### 2. API REST 🌐
- **Estrutura:** APIs RESTful para comunicação eficiente entre cliente e servidor.
- **Endpoints:** Definidos conforme a necessidade para garantir clareza e coesão.

### 3. Banco de Dados 🗄️
- **DynamoDB:** Escolhido pela escalabilidade e baixa latência na AWS, ideal para aplicações de alto desempenho.
  - **O que é o Amazon DynamoDB?** 🏦
    - ✔ **Banco de dados sem servidor:** Sem necessidade de provisionamento, aplicação de patches ou gerenciamento de servidores. Não há software para instalar, manter ou operar, com manutenção sem tempo de inatividade.
    - ✔ **Alta disponibilidade:** Tabelas globais fornecem um banco de dados multirregional e multiativo com SLA de disponibilidade de até 99,999%, oferecendo leituras e gravações locais rápidas e maior resiliência do aplicativo.
    - ✔ **Confiabilidade:** Suportada por backups gerenciados, recuperação pontual e um amplo conjunto de controles de segurança e padrões de conformidade.

### 4. Dependências 🚀

1. **Spring Boot Starter Web** 🌐
  - **Por quê?** Facilita a criação de aplicações web com configuração pronta para uso.

2. **Lombok** 📝
  - **Por quê?** Simplifica o código, gerando getters, setters e outros métodos automaticamente.

3. **Spring Boot Starter Test** 🧪
  - **Por quê?** Inclui bibliotecas essenciais para testes, garantindo a qualidade do código.

4. **Spring Boot Starter Validation** ✅
  - **Por quê?** Facilita a validação de dados, garantindo que as entradas da API sejam corretas.

5. **Spring Cloud AWS Starter DynamoDB** 🗄️
  - **Por quê?** Integra facilmente com o DynamoDB, simplificando o acesso e operações no banco de dados NoSQL.

6. **Google Maps Services** 🗺️
  - **Por quê?** Facilita a integração com a API do Google Maps, útil para obter informações de localização.

7. **Geo** 🌍
  - **Por quê?** Oferece funcionalidades geoespaciais, ajudando a lidar com coordenadas e cálculos de distância.

8. **Logback** 📋
  - **Por quê?** Biblioteca de logs do Spring Boot, oferecendo recursos de log eficientes.

### 5. Instruções para Executar os Testes da Solução 🧪

1. **Pré-requisitos** 📋
  - **Java 21**: Certifique-se de ter o JDK 21 instalado. [Download](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
  - **Maven**: Ferramenta de build para gerenciar dependências e compilar o projeto. [Download](https://maven.apache.org/download.cgi)

2. **Clonar o Repositório** 📂
  - Clone o repositório do projeto para sua máquina local:
    ```bash
    git clone https://github.com/wscatao/NearbyAgency.git
    ```

3. **Compilar o Projeto** ⚙️
  - No diretório raiz do projeto, execute o comando Maven para compilar:
    ```bash
    mvn clean install
    ```

4. **Executar os Testes** ▶️
  - Para executar os testes unitários e de integração, utilize o comando Maven:
    ```bash
    mvn test
    ```
  - Esse comando irá compilar e executar todos os testes definidos no projeto.

5. **Verificar Resultados** 📋
  - Após a execução dos testes, o Maven gerará um relatório com os resultados.
  - Verifique o console para visualizar um resumo dos testes executados e seus respectivos resultados.
  - Relatórios detalhados também podem ser encontrados no diretório `target/surefire-reports`.

## Considerações Finais 📝
As decisões técnicas e arquiteturais são baseadas em práticas recomendadas e requisitos do projeto, visando criar uma aplicação robusta, escalável e de fácil manutenção.
