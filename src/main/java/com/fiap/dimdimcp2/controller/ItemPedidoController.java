package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.dto.NovoItemPedidoDTO;
import com.fiap.dimdimcp2.model.ItemPedido;
import com.fiap.dimdimcp2.model.Pedido;
import com.fiap.dimdimcp2.repository.ItemPedidoRepository;
import com.fiap.dimdimcp2.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/itens")
public class ItemPedidoController {

    private final ItemPedidoRepository itemRepo;
    private final PedidoRepository pedidoRepo;

    public ItemPedidoController(ItemPedidoRepository itemRepo, PedidoRepository pedidoRepo) {
        this.itemRepo = itemRepo;
        this.pedidoRepo = pedidoRepo;
    }

    // GET /api/itens -> lista todos os itens
    @GetMapping
    public List<ItemPedido> listarTodos() {
        return itemRepo.findAll();
    }

    // GET /api/itens/{id} -> busca um item específico
    @GetMapping("/{id}")
    public ResponseEntity<ItemPedido> buscarPorId(@PathVariable Long id) {
        return itemRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/itens -> cria um item em um pedido existente
    @PostMapping
    public ResponseEntity<ItemPedido> criar(@RequestBody @Valid NovoItemPedidoDTO dto) {
        Pedido pedido = pedidoRepo.findById(dto.pedidoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não encontrado"));

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setDescricao(dto.descricao());
        item.setQuantidade(dto.quantidade());
        item.setValorUnitario(dto.valorUnitario());

        ItemPedido salvo = itemRepo.save(item);

        return ResponseEntity
                .created(URI.create("/api/itens/" + salvo.getId()))
                .body(salvo);
    }

    // DELETE /api/itens/{id} -> remove um item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!itemRepo.existsById(id)) return ResponseEntity.notFound().build();
        itemRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
