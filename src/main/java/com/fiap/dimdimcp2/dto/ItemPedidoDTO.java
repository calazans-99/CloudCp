package com.fiap.dimdimcp2.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
        Long id,
        String descricao,
        Integer quantidade,
        BigDecimal valorUnitario
) {}
