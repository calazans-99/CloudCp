package com.fiap.cloudcp.dto.pedido;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoCreateDTO(
        @NotNull Long clienteId,
        List<ItemCreateDTO> itens
) {}
