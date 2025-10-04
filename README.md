# CloudCp (Java 21, Spring Boot 3.3.5)

API do CP2 (Clientes e Pedidos) preparada para Azure App Service + Azure SQL.

## Requisitos
- JDK 21
- Maven 3.9+
- Azure SQL (tabelas com `datetimeoffset(6)` para `criado_em`)

## Rodar local
```bash
mvn clean package -DskipTests
java -jar target/cloudcp-0.0.1-SNAPSHOT.jar
```
Swagger: http://localhost:8080/swagger-ui.html

## Configuração Azure SQL
Edite `src/main/resources/application.properties` com seu servidor, DB, usuário e senha.

## Endpoints principais
- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `POST /api/pedidos`
- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `PATCH /api/pedidos/{id}/status`
- `POST /api/pedidos/{id}/itens`
- `DELETE /api/pedidos/{id}`

## Observações
- Campos `criado_em` mapeados para `datetimeoffset(6)` e `OffsetDateTime` no Java.
- `ddl-auto=validate` (recomendado em prod). Em dev, pode usar `update`.
