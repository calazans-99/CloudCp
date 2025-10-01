package com.fiap.dimdimcp2.dto;

import com.fiap.dimdimcp2.model.enums.PedidoStatus;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Campos nulos NÃO são alterados.
 * - status: se vier não-nulo, atualiza.
 * - itens:  se vier não-nulo, substitui todos os itens do pedido.
 */
public record AtualizarPedidoDTO(
        PedidoStatus status,
        @Valid List<NovoItemPedidoDTO> itens
) {}
