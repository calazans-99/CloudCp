package com.fiap.cloudcp.dto.pedido;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemCreateDTO(
        @NotBlank String descricao,
        @NotNull @Positive Integer quantidade,
        @NotNull @DecimalMin("0.00") BigDecimal valorUnitario
) {}
