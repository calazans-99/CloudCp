package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record NovoItemPedidoDTO(
        @NotBlank @Size(max = 120) String descricao,
        @NotNull @Positive Integer quantidade,
        @NotNull @PositiveOrZero BigDecimal valorUnitario
) {}
