package com.fiap.cloudcp.service.impl;

import com.fiap.cloudcp.dto.pedido.*;
import com.fiap.cloudcp.mapper.PedidoMapper;
import com.fiap.cloudcp.model.Cliente;
import com.fiap.cloudcp.model.ItemPedido;
import com.fiap.cloudcp.model.Pedido;
import com.fiap.cloudcp.model.enums.PedidoStatus;
import com.fiap.cloudcp.repository.ClienteRepository;
import com.fiap.cloudcp.repository.ItemPedidoRepository;
import com.fiap.cloudcp.repository.PedidoRepository;
import com.fiap.cloudcp.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ClienteRepository clienteRepo;
    private final ItemPedidoRepository itemRepo;
    private final PedidoMapper mapper;

    public PedidoServiceImpl(PedidoRepository p, ClienteRepository c, ItemPedidoRepository i, PedidoMapper m){
        this.pedidoRepo = p; this.clienteRepo = c; this.itemRepo = i; this.mapper = m;
    }

    @Override @Transactional
    public PedidoDTO criar(PedidoCreateDTO dto) {
        Cliente cliente = clienteRepo.findById(dto.clienteId()).orElseThrow();

        Pedido p = new Pedido();
        p.setCliente(cliente);
        p.setStatus(PedidoStatus.NOVO);
        p.setTotal(BigDecimal.ZERO);

        if (dto.itens() != null) {
            for (ItemCreateDTO ni : dto.itens()) {
                ItemPedido it = new ItemPedido();
                it.setPedido(p);
                it.setDescricao(ni.descricao());
                it.setQuantidade(ni.quantidade());
                it.setValorUnitario(ni.valorUnitario());
                p.addItem(it);
            }
        }

        BigDecimal total = p.getItens().stream()
                .map(i -> i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        p.setTotal(total);

        var saved = pedidoRepo.save(p);
        return mapper.toDTO(saved);
    }

    @Override
    public List<PedidoDTO> listar() {
        return pedidoRepo.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public PedidoDTO obter(Long id) {
        var p = pedidoRepo.findById(id).orElseThrow();
        return mapper.toDTO(p);
    }

    @Override @Transactional
    public PedidoDTO atualizarStatus(Long id, PedidoStatus status) {
        var p = pedidoRepo.findById(id).orElseThrow();
        p.setStatus(status);
        return mapper.toDTO(p);
    }

    @Override @Transactional
    public PedidoDTO adicionarItem(Long id, ItemCreateDTO dto) {
        var p = pedidoRepo.findById(id).orElseThrow();
        ItemPedido it = new ItemPedido();
        it.setPedido(p);
        it.setDescricao(dto.descricao());
        it.setQuantidade(dto.quantidade());
        it.setValorUnitario(dto.valorUnitario());
        itemRepo.save(it);
        p.getItens().add(it);

        var total = p.getItens().stream()
                .map(i -> i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        p.setTotal(total);

        return mapper.toDTO(p);
    }

    @Override @Transactional
    public void excluir(Long id) {
        pedidoRepo.deleteById(id);
    }
}
