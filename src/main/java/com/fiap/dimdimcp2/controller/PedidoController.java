package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.model.ItemPedido;
import com.fiap.dimdimcp2.model.Pedido;
import com.fiap.dimdimcp2.dto.AtualizarPedidoDTO;
import com.fiap.dimdimcp2.dto.AtualizarStatusDTO;
import com.fiap.dimdimcp2.dto.NovoItemPedidoDTO;
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
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ItemPedidoRepository itens;

    public PedidoController(PedidoRepository pedidos,
                            ClienteRepository clientes,
                            ItemPedidoRepository itens) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.itens = itens;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidos.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return pedidos.findWithClienteAndItensById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> criar(@RequestBody @Valid NovoPedidoDTO dto) {
        var c = clientes.findById(dto.clienteId());
        if (c.isEmpty()) return ResponseEntity.unprocessableEntity().build();

        var pedido = new Pedido();
        pedido.setCliente(c.get());

        for (NovoItemPedidoDTO i : dto.itens()) {
            var it = toItem(i);
            it.setPedido(pedido);
            pedido.getItens().add(it);
        }
        pedido.recalcularTotal();

        var salvo = pedidos.save(pedido);
        return ResponseEntity.created(URI.create("/api/v1/pedidos/" + salvo.getId()))
                .body(salvo);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id,
                                            @RequestBody @Valid AtualizarPedidoDTO dto) {
        return pedidos.findWithClienteAndItensById(id)
                .map(p -> {
                    if (dto.status() != null) p.setStatus(dto.status());
                    if (dto.itens() != null) {
                        p.getItens().clear();
                        for (NovoItemPedidoDTO i : dto.itens()) {
                            var it = toItem(i);
                            it.setPedido(p);
                            p.getItens().add(it);
                        }
                        p.recalcularTotal();
                    }
                    return ResponseEntity.ok(p);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id,
                                                  @RequestBody @Valid AtualizarStatusDTO dto) {
        return pedidos.findById(id)
                .map(p -> {
                    p.setStatus(dto.status());
                    return ResponseEntity.ok(p);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemPedido>> listarItens(@PathVariable Long id) {
        if (!pedidos.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(itens.findByPedidoId(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        if (!pedidos.existsById(id)) return ResponseEntity.notFound().build();
        pedidos.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ItemPedido toItem(NovoItemPedidoDTO i) {
        var it = new ItemPedido();
        it.setDescricao(i.descricao());
        it.setQuantidade(i.quantidade());
        it.setValorUnitario(i.valorUnitario());
        return it;
    }
}
