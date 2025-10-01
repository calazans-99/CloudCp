-- V1__ddl_dimdim.sql (com ON DELETE CASCADE)
-- Apaga pedidos ao apagar cliente; apaga itens ao apagar pedido.

IF OBJECT_ID('dbo.cliente', 'U') IS NULL
BEGIN
  CREATE TABLE dbo.cliente (
    id         BIGINT IDENTITY(1,1) CONSTRAINT PK_cliente PRIMARY KEY,
    nome       NVARCHAR(120) NOT NULL,
    email      NVARCHAR(120) NOT NULL,
    criado_em  DATETIMEOFFSET NOT NULL CONSTRAINT DF_cliente_criado_em DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT UQ_cliente_email UNIQUE (email)
  );
END;

IF OBJECT_ID('dbo.pedido', 'U') IS NULL
BEGIN
  CREATE TABLE dbo.pedido (
    id           BIGINT IDENTITY(1,1) CONSTRAINT PK_pedido PRIMARY KEY,
    cliente_id   BIGINT NOT NULL,
    data_pedido  DATETIMEOFFSET NOT NULL CONSTRAINT DF_pedido_data DEFAULT SYSDATETIMEOFFSET(),
    status       NVARCHAR(30) NOT NULL,
    CONSTRAINT FK_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES dbo.cliente(id) ON DELETE CASCADE,
    CONSTRAINT CK_pedido_status CHECK (status IN (N'NOVO', N'PAGO', N'CANCELADO'))
  );
END;

IF OBJECT_ID('dbo.item_pedido', 'U') IS NULL
BEGIN
  CREATE TABLE dbo.item_pedido (
    id             BIGINT IDENTITY(1,1) CONSTRAINT PK_item_pedido PRIMARY KEY,
    pedido_id      BIGINT NOT NULL,
    descricao      NVARCHAR(120) NOT NULL,
    quantidade     INT NOT NULL,
    valor_unitario DECIMAL(12,2) NOT NULL,
    CONSTRAINT FK_item_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES dbo.pedido(id) ON DELETE CASCADE,
    CONSTRAINT CK_item_pedido_qtd CHECK (quantidade > 0),
    CONSTRAINT CK_item_pedido_valor CHECK (valor_unitario >= 0)
  );
END;

-- Índices
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_cliente_email' AND object_id = OBJECT_ID('dbo.cliente'))
  CREATE UNIQUE INDEX IX_cliente_email ON dbo.cliente(email);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_pedido_cliente' AND object_id = OBJECT_ID('dbo.pedido'))
  CREATE INDEX IX_pedido_cliente ON dbo.pedido(cliente_id);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_pedido_status' AND object_id = OBJECT_ID('dbo.pedido'))
  CREATE INDEX IX_pedido_status ON dbo.pedido(status);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_item_pedido_pedido' AND object_id = OBJECT_ID('dbo.item_pedido'))
  CREATE INDEX IX_item_pedido_pedido ON dbo.item_pedido(pedido_id);
