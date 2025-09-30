# DimDim CP2 — Java (Spring Boot) + Azure SQL

App de exemplo para o Checkpoint 2 (DimDim): API REST com `cliente`, `pedido`, `item_pedido`.
- Java 17 + Spring Boot 3
- Azure SQL (PaaS)
- Flyway (DDL em `V1__ddl_dimdim.sql`)
- Swagger UI em `/swagger-ui/index.html`

## Rodando local (opcional)
1. Defina as variáveis de ambiente conforme o deploy no Azure App Service:
   ```bash
   export SPRING_DATASOURCE_URL="jdbc:sqlserver://<server>.database.windows.net:1433;database=<db>;user=<user>@<server>;password=<pwd>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
   export SPRING_DATASOURCE_USERNAME="<user>"
   export SPRING_DATASOURCE_PASSWORD="<pwd>"
   ```
2. Rode:
   ```bash
   ./mvnw spring-boot:run
   ```

## Endpoints (exemplos)
- `GET /api/clientes`
- `POST /api/clientes` body:
  ```json
  {"nome":"Alice","email":"alice@fiap.com"}
  ```
- `POST /api/pedidos` body:
  ```json
  {"clienteId":1, "status":"ABERTO"}
  ```
- `POST /api/pedidos/{id}/itens` body:
  ```json
  {"pedidoId":1, "descricao":"Mensalidade", "quantidade":1, "valorUnitario":100.00}
  ```

## Observações
- Em produção, troque a senha do DB e restrinja o firewall do Azure SQL a partir do seu IP/App Service.
- Flyway valida o schema ao subir (`ddl-auto=validate`).
