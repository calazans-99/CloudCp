package com.fiap.dimdimcp2.dto;

import com.fiap.dimdimcp2.model.enums.PedidoStatus;

public record AtualizarPedidoDTO(
        PedidoStatus status,
        java.util.List<NovoItemPedidoDTO> itens
) {}
