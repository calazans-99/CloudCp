// src/main/java/com/fiap/dimdimcp2/dto/NovoItemPedidoDTO.java
package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record NovoItemPedidoDTO(
        @NotNull Long pedidoId,
        @NotBlank String descricao,
        @Min(1) int quantidade,
        @NotNull BigDecimal valorUnitario
) { }
