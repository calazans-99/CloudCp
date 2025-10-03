# Cloud CP Java (Spring Boot)

Projeto exemplo pronto para Azure App Service (Java SE, JAR).

## Build & Run
```bash
mvn -DskipTests package
java -jar target/cloudcp-app-0.0.1-SNAPSHOT.jar
```
Acesse: http://localhost:8080/swagger-ui.html

## Banco de Dados (Azure SQL)
Defina:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

As entidades usam `OffsetDateTime` + `datetimeoffset(6)`. O Hibernate cria/atualiza o schema (`ddl-auto=update`).
