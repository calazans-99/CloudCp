package com.fiap.cloudcp.dto.pedido;

import com.fiap.cloudcp.model.enums.PedidoStatus;
import jakarta.validation.constraints.NotNull;

public record PedidoStatusUpdateDTO(@NotNull PedidoStatus status) {}
