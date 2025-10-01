package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.model.ItemPedido;
import com.fiap.dimdimcp2.model.Pedido;
import com.fiap.dimdimcp2.dto.NovoPedidoDTO;
import com.fiap.dimdimcp2.repository.ClienteRepository;
import com.fiap.dimdimcp2.repository.ItemPedidoRepository;
import com.fiap.dimdimcp2.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ItemPedidoRepository itensRepo;

    public PedidoController(PedidoRepository pedidos,
                            ClienteRepository clientes,
                            ItemPedidoRepository itensRepo) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.itensRepo = itensRepo;
    }

    @GetMapping
    public List<Pedido> listar() {
        // EntityGraph em findAll() evita N+1 e LazyInitializationException
        return pedidos.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> criar(@RequestBody @Valid NovoPedidoDTO body) {
        var cliente = clientes.findById(body.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + body.clienteId()));

        var pedido = new Pedido();
        pedido.setCliente(cliente);

        var itens = body.itens().stream().map(i -> {
            var it = new ItemPedido();
            it.setDescricao(i.descricao());
            it.setQuantidade(i.quantidade());
            it.setValorUnitario(i.valorUnitario());
            return it;
        }).toList();

        pedido.addItens(itens);

        var salvo = pedidos.save(pedido);
        return ResponseEntity.created(URI.create("/api/v1/pedidos/" + salvo.getId())).body(salvo);
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemPedido>> listarItens(@PathVariable Long id) {
        if (!pedidos.existsById(id)) return ResponseEntity.notFound().build();
        // Evita LazyInitializationException sem depender de sessão aberta
        var itens = itensRepo.findByPedidoId(id);
        return ResponseEntity.ok(itens);
    }
}
