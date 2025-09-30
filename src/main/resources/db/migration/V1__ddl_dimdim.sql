-- V1__ddl_dimdim.sql
CREATE TABLE cliente (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  nome NVARCHAR(120) NOT NULL,
  email NVARCHAR(120) UNIQUE NOT NULL,
  criado_em DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET()
);

CREATE TABLE pedido (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  cliente_id BIGINT NOT NULL,
  data_pedido DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
  status NVARCHAR(30) NOT NULL,
  CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE item_pedido (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  pedido_id BIGINT NOT NULL,
  descricao NVARCHAR(120) NOT NULL,
  quantidade INT NOT NULL CHECK (quantidade > 0),
  valor_unitario DECIMAL(12,2) NOT NULL CHECK (valor_unitario >= 0),
  CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);
