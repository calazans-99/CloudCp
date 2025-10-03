package com.fiap.cloudcp.mapper;

import com.fiap.cloudcp.dto.cliente.ClienteDTO;
import com.fiap.cloudcp.dto.pedido.ItemDTO;
import com.fiap.cloudcp.dto.pedido.PedidoDTO;
import com.fiap.cloudcp.model.Cliente;
import com.fiap.cloudcp.model.ItemPedido;
import com.fiap.cloudcp.model.Pedido;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class PedidoMapper {

    /* =========================
       Helpers de data
       ========================= */

    // Quando a entidade usa LocalDateTime (sem fuso), padroniza como UTC
    private OffsetDateTime toOffset(LocalDateTime ldt) {
        return (ldt == null) ? null : ldt.atOffset(ZoneOffset.UTC);
    }

    // Quando a entidade já usa OffsetDateTime, só repassa
    private OffsetDateTime toOffset(OffsetDateTime odt) {
        return odt;
    }

    /* =========================
       Entidade -> DTO
       ========================= */

    public ClienteDTO toDTO(Cliente c) {
        if (c == null) return null;
        return new ClienteDTO(
                c.getId(),
                c.getNome(),
                c.getEmail(),
                toOffset(c.getCriadoEm())
        );
    }

    public ItemDTO toDTO(ItemPedido i) {
        if (i == null) return null;
        return new ItemDTO(
                i.getId(),
                i.getDescricao(),
                i.getQuantidade(),
                i.getValorUnitario()
        );
    }

    public List<ItemDTO> toItemDTOList(List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) return Collections.emptyList();
        return itens.stream()
                .filter(Objects::nonNull)
                .map(this::toDTO)
                .toList();
    }

    public PedidoDTO toDTO(Pedido p) {
        if (p == null) return null;
        return new PedidoDTO(
                p.getId(),
                toDTO(p.getCliente()),
                toItemDTOList(p.getItens()),
                p.getStatus(),
                p.getTotal(),
                toOffset(p.getCriadoEm())
        );
    }

    /* =========================
       (Opcional) listas de pedidos
       ========================= */

    public List<PedidoDTO> toPedidoDTOList(List<Pedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) return Collections.emptyList();
        return pedidos.stream()
                .filter(Objects::nonNull)
                .map(this::toDTO)
                .toList();
    }
}
