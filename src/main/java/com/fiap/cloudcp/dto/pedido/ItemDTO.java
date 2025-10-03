package com.fiap.cloudcp.dto.pedido;

import java.math.BigDecimal;

public record ItemDTO(
        Long id,
        String descricao,
        Integer quantidade,
        BigDecimal valorUnitario
) {}
