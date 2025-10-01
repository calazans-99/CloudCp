package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record NovoItemPedidoDTO(
        @NotBlank @Size(max = 120) String descricao,
        @Positive Integer quantidade,
        @PositiveOrZero BigDecimal valorUnitario
) {}
