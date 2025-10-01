package com.fiap.dimdimcp2.dto;

import com.fiap.dimdimcp2.model.enums.PedidoStatus;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusDTO(@NotNull PedidoStatus status) {}
