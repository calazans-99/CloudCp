package com.fiap.cloudcp.dto.pedido;

import com.fiap.cloudcp.dto.cliente.ClienteDTO;
import com.fiap.cloudcp.model.enums.PedidoStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record PedidoDTO(
        Long id,
        ClienteDTO cliente,
        List<ItemDTO> itens,
        PedidoStatus status,
        BigDecimal total,
        OffsetDateTime criadoEm
) {}
