package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record NovoItemPedidoDTO(
        @NotBlank String descricao,
        @NotNull @Min(1) Integer quantidade,
        @NotNull @PositiveOrZero BigDecimal valorUnitario
) {}
