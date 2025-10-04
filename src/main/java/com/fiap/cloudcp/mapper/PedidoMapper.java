package com.fiap.cloudcp.mapper;

import com.fiap.cloudcp.dto.cliente.ClienteDTO;
import com.fiap.cloudcp.dto.pedido.ItemDTO;
import com.fiap.cloudcp.dto.pedido.PedidoDTO;
import com.fiap.cloudcp.model.Cliente;
import com.fiap.cloudcp.model.ItemPedido;
import com.fiap.cloudcp.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public ClienteDTO toDTO(Cliente c) {
        if (c == null) return null;
        return new ClienteDTO(c.getId(), c.getNome(), c.getEmail(), c.getCriadoEm());
    }

    public ItemDTO toDTO(ItemPedido i) {
        if (i == null) return null;
        return new ItemDTO(i.getId(), i.getDescricao(), i.getQuantidade(), i.getValorUnitario());
    }

    public PedidoDTO toDTO(Pedido p) {
        if (p == null) return null;
        List<ItemDTO> itens = p.getItens().stream().map(this::toDTO).toList();
        return new PedidoDTO(
                p.getId(),
                toDTO(p.getCliente()),
                itens,
                p.getStatus(),
                p.getTotal(),
                p.getCriadoEm()
        );
    }
}
