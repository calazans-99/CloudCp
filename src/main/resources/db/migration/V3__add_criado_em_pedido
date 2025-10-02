IF COL_LENGTH('dbo.pedido', 'criado_em') IS NULL
BEGIN
  ALTER TABLE dbo.pedido
    ADD criado_em DATETIME2(0) NOT NULL
        CONSTRAINT DF_pedido_criado_em DEFAULT SYSUTCDATETIME()
        WITH VALUES; -- preenche linhas existentes
END
