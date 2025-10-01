-- CHECK do status, índices úteis e nomes explícitos de constraints

IF NOT EXISTS (
  SELECT 1 FROM sys.check_constraints
  WHERE name = 'CK_pedido_status' AND parent_object_id = OBJECT_ID('dbo.pedido')
)
BEGIN
  ALTER TABLE dbo.pedido
    ADD CONSTRAINT CK_pedido_status CHECK (status IN (N'NOVO', N'PAGO', N'CANCELADO'));
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_cliente_email' AND object_id = OBJECT_ID('dbo.cliente'))
  CREATE UNIQUE INDEX IX_cliente_email ON dbo.cliente(email);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_pedido_cliente' AND object_id = OBJECT_ID('dbo.pedido'))
  CREATE INDEX IX_pedido_cliente ON dbo.pedido(cliente_id);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_pedido_status' AND object_id = OBJECT_ID('dbo.pedido'))
  CREATE INDEX IX_pedido_status ON dbo.pedido(status);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_item_pedido_pedido' AND object_id = OBJECT_ID('dbo.item_pedido'))
  CREATE INDEX IX_item_pedido_pedido ON dbo.item_pedido(pedido_id);
