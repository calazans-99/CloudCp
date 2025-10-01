package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record NovoPedidoDTO(
        @NotNull Long clienteId,
        @NotBlank String status
) { }
