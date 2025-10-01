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

import java.util.*;

@RestController
@RequestMapping("/api/pedidos")
public class ItemPedidoController {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoController(PedidoRepository pedidoRepository,
                                ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<?> adicionarItem(@PathVariable("id") Long id,
                                           @RequestBody @Valid NovoItemPedidoDTO dto) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Pedido não encontrado", "id", id));
        }

        Pedido pedido = pedidoOpt.get();

        ItemPedido item = new ItemPedido();
        item.setDescricao(dto.getDescricao());
        item.setQuantidade(dto.getQuantidade());
        item.setValorUnitario(dto.getValorUnitario());
        item.setPedido(pedido);

        // Persistimos o item
        item = itemPedidoRepository.save(item);

        // Mantém o lado dono e o inverso do relacionamento sincronizado
        if (pedido.getItens() == null) {
            pedido.setItens(new ArrayList<>());
        }
        pedido.getItens().add(item);
        pedidoRepository.save(pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<?> listarItens(@PathVariable("id") Long id) {
        return pedidoRepository.findById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(p.getItens()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Pedido não encontrado", "id", id)));
    }
}
