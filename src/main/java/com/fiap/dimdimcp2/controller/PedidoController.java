package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.dto.NovoItemPedidoDTO;
import com.fiap.dimdimcp2.dto.NovoPedidoDTO;
import com.fiap.dimdimcp2.model.Cliente;
import com.fiap.dimdimcp2.model.ItemPedido;
import com.fiap.dimdimcp2.model.Pedido;
import com.fiap.dimdimcp2.repository.ClienteRepository;
import com.fiap.dimdimcp2.repository.ItemPedidoRepository;
import com.fiap.dimdimcp2.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // <--- importe aqui
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ItemPedidoRepository itens;

    public PedidoController(PedidoRepository pedidos, ClienteRepository clientes, ItemPedidoRepository itens) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.itens = itens;
    }

    @Transactional(readOnly = true) // <--- ajuste
    @GetMapping
    public List<Pedido> listar() {
        return pedidos.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid NovoPedidoDTO dto) {
        Optional<Cliente> c = clientes.findById(dto.clienteId());
        if (c.isEmpty()) return ResponseEntity.badRequest().body("clienteId inexistente");

        Pedido p = new Pedido();
        p.setCliente(c.get());
        p.setStatus(dto.status());
        Pedido salvo = pedidos.save(p);
        return ResponseEntity.created(URI.create("/api/pedidos/" + salvo.getId())).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obter(@PathVariable("id") Long id) {
        return pedidos.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/itens")
    @Transactional
    public ResponseEntity<?> adicionarItem(
            @PathVariable("id") Long id,
            @RequestBody @Valid NovoItemPedidoDTO dto) {

        // valida se o pedido do path e do body são iguais
        if (!id.equals(dto.pedidoId())) {
            return ResponseEntity.badRequest().body("O pedidoId do body difere do path");
        }

        Optional<Pedido> pedidoOpt = pedidos.findById(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido pedido = pedidoOpt.get();
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setDescricao(dto.descricao());
        item.setQuantidade(dto.quantidade());
        item.setValorUnitario(
                dto.valorUnitario() == null ? BigDecimal.ZERO : dto.valorUnitario()
        );

        ItemPedido salvo = itens.save(item);

        return ResponseEntity
                .created(URI.create("/api/pedidos/" + id + "/itens/" + salvo.getId()))
                .body(salvo);
    }
}