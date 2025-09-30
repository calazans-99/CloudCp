package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NovoPedidoDTO(
    @NotNull Long clienteId,
    @NotBlank String status
) {}
