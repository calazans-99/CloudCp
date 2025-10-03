package com.fiap.cloudcp.service;

import com.fiap.cloudcp.dto.pedido.ItemCreateDTO;
import com.fiap.cloudcp.dto.pedido.PedidoCreateDTO;
import com.fiap.cloudcp.model.Cliente;
import com.fiap.cloudcp.model.ItemPedido;
import com.fiap.cloudcp.model.Pedido;
import com.fiap.cloudcp.model.enums.PedidoStatus;
import com.fiap.cloudcp.repository.ClienteRepository;
import com.fiap.cloudcp.repository.ItemPedidoRepository;
import com.fiap.cloudcp.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ClienteRepository clienteRepo;
    private final ItemPedidoRepository itemRepo;

    public PedidoService(PedidoRepository pedidoRepo, ClienteRepository clienteRepo, ItemPedidoRepository itemRepo) {
        this.pedidoRepo = pedidoRepo;
        this.clienteRepo = clienteRepo;
        this.itemRepo = itemRepo;
    }

    public Pedido criar(PedidoCreateDTO dto) {
        Cliente cliente = clienteRepo.findById(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        var p = new Pedido();
        p.setCliente(cliente);
        p.setStatus(PedidoStatus.NOVO);
        p.setItens(new ArrayList<>());

        dto.itens().forEach(i -> {
            var item = new ItemPedido();
            item.setPedido(p);
            item.setDescricao(i.descricao());
            item.setQuantidade(i.quantidade());
            item.setValorUnitario(i.valorUnitario());
            p.getItens().add(item);
        });

        p.recalcularTotal();
        return pedidoRepo.save(p);
    }

    public Pedido atualizarStatus(Long id, PedidoStatus status) {
        var p = pedidoRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        p.setStatus(status);
        return p;
    }

    public Pedido adicionarItem(Long pedidoId, ItemCreateDTO dto) {
        var p = pedidoRepo.findById(pedidoId).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        var item = new ItemPedido();
        item.setPedido(p);
        item.setDescricao(dto.descricao());
        item.setQuantidade(dto.quantidade());
        item.setValorUnitario(dto.valorUnitario());
        p.getItens().add(item);
        p.recalcularTotal();
        return p;
    }

    @Transactional(readOnly = true)
    public Pedido obter(Long id) {
        return pedidoRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listar() {
        return pedidoRepo.findAll();
    }

    public void excluir(Long id) {
        if (!pedidoRepo.existsById(id)) throw new EntityNotFoundException("Pedido não encontrado");
        pedidoRepo.deleteById(id);
    }
}
