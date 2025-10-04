package com.fiap.cloudcp.dto.pedido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemCreateDTO(
        @NotBlank String descricao,
        @Min(1) Integer quantidade,
        @NotNull BigDecimal valorUnitario
) {}
