package com.fiap.dimdimcp2.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Long clienteId,
        String clienteNome,
        String status,
        BigDecimal total,
        LocalDateTime criadoEm,
        List<ItemPedidoDTO> itens
) {}
