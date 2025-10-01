package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record NovoItemPedidoDTO(
        @NotBlank String descricao,
        @NotNull @Positive Integer quantidade,
        @NotNull @Positive BigDecimal valorUnitario
) { }
