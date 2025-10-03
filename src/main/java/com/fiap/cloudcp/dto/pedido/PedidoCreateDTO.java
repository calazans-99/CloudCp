package com.fiap.cloudcp.dto.pedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PedidoCreateDTO(
        @NotNull Long clienteId,
        @NotNull @Size(min = 1) List<ItemCreateDTO> itens
) {}
