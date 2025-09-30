package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record NovoItemPedidoDTO(
    @NotNull Long pedidoId,
    @NotBlank String descricao,
    @Min(1) Integer quantidade,
    @PositiveOrZero BigDecimal valorUnitario
) {}
