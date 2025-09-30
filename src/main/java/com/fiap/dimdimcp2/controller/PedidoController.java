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
import org.springframework.transaction.annotation.Transactional;
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
    public ResponseEntity<Pedido> obter(@PathVariable Long id) {
        return pedidos.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/itens")
    @Transactional
    public ResponseEntity<?> adicionarItem(@PathVariable Long id, @RequestBody @Valid NovoItemPedidoDTO dto) {
        if (!id.equals(dto.pedidoId())) return ResponseEntity.badRequest().body("pedidoId do body difere do path");
        Optional<Pedido> p = pedidos.findById(id);
        if (p.isEmpty()) return ResponseEntity.notFound().build();

        ItemPedido item = new ItemPedido();
        item.setPedido(p.get());
        item.setDescricao(dto.descricao());
        item.setQuantidade(dto.quantidade());
        item.setValorUnitario(dto.valorUnitario() == null ? BigDecimal.ZERO : dto.valorUnitario());
        ItemPedido salvo = itens.save(item);
        return ResponseEntity.created(URI.create("/api/pedidos/%d/itens/%d".formatted(id, salvo.getId()))).body(salvo);
    }
}
