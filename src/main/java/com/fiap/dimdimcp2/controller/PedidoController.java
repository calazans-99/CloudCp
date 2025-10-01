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

    // GET /pedidos
    @GetMapping
    public List<Pedido> listar() {
        // Dica: no PedidoRepository, adicione @EntityGraph({"cliente","itens"}) ao findAll()
        return pedidos.findAll();
    }

    // GET /pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obter(@PathVariable("id") Long id) {
        return pedidos.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /pedidos/{id}/itens
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemPedido>> listarItens(@PathVariable("id") Long id) {
        if (!pedidos.existsById(id)) return ResponseEntity.notFound().build();
        var itens = itensRepo.findByPedidoId(id); // evita LazyInitializationException com OSIV=false
        return ResponseEntity.ok(itens);
    }

    // POST /pedidos
    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> criar(@RequestBody @Valid NovoPedidoDTO body) {
        var cliente = clientes.findById(body.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + body.clienteId()));

        var pedido = new Pedido();
        pedido.setCliente(cliente);

        var itens = body.itens().stream().map(this::toItem).toList();
        pedido.addItens(itens);

        var salvo = pedidos.save(pedido);
        return ResponseEntity.created(URI.create("/api/v1/pedidos/" + salvo.getId())).body(salvo);
    }

    // PUT /pedidos/{id}
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Pedido> atualizar(@PathVariable("id") Long id,
                                            @RequestBody @Valid AtualizarPedidoDTO body) {
        return pedidos.findById(id)
                .map(p -> {
                    if (body.status() != null) {
                        p.setStatus(body.status());
                    }
                    if (body.itens() != null) {
                        p.getItens().clear();
                        var novos = body.itens().stream().map(this::toItem).toList();
                        p.addItens(novos);
                    }
                    return ResponseEntity.ok(p);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /pedidos/{id}/status
    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable("id") Long id,
                                                  @RequestBody @Valid AtualizarStatusDTO body) {
        return pedidos.findById(id)
                .map(p -> {
                    p.setStatus(body.status());
                    return ResponseEntity.ok(p);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /pedidos/{id}
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
