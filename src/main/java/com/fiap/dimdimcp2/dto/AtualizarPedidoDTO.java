package com.fiap.dimdimcp2.dto;

import com.fiap.dimdimcp2.model.enums.PedidoStatus;
import jakarta.validation.Valid;
import java.util.List;

/** Campos nulos NÃO são alterados. */
public record AtualizarPedidoDTO(
        PedidoStatus status,
        @Valid List<NovoItemPedidoDTO> itens
) {}
