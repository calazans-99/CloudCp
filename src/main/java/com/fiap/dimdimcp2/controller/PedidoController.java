package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.dto.*;
import com.fiap.dimdimcp2.model.ItemPedido;
import com.fiap.dimdimcp2.model.Pedido;
import com.fiap.dimdimcp2.model.enums.PedidoStatus;
import com.fiap.dimdimcp2.repository.ClienteRepository;
import com.fiap.dimdimcp2.repository.ItemPedidoRepository;
import com.fiap.dimdimcp2.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    // ---------- MAPEADORES (Entity -> DTO) ----------
    private ItemPedidoDTO toDTO(ItemPedido i) {
        return new ItemPedidoDTO(
                i.getId(),
                i.getDescricao(),
                i.getQuantidade(),
                i.getValorUnitario()
        );
    }

    private PedidoResponseDTO toDTO(Pedido p) {
        var itensDTO = p.getItens().stream().map(this::toDTO).toList();
        return new PedidoResponseDTO(
                p.getId(),
                p.getCliente().getId(),
                p.getCliente().getNome(),
                p.getStatus().name(),
                p.getTotal(),
                p.getCriadoEm().toLocalDateTime(),
                itensDTO
        );
    }

    // ---------- ENDPOINTS ----------

    /** Lista todos os pedidos já com cliente + itens (via @EntityGraph no repository). */
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        var pedidos = pedidoRepository.findAll();
        var body = pedidos.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(body);
    }

    /** Busca um pedido específico com cliente + itens (via método dedicado no repository). */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscar(@PathVariable Long id) {
        var pedido = pedidoRepository.findWithClienteAndItensById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        return ResponseEntity.ok(toDTO(pedido));
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemPedidoDTO>> listarItens(@PathVariable Long id) {
        var pedido = pedidoRepository.findWithClienteAndItensById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        var itens = pedido.getItens().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(itens);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid NovoPedidoDTO dto,
                                                   UriComponentsBuilder uri) {
        var cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + dto.clienteId()));

        var pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(PedidoStatus.NOVO);

        // monta itens
        List<ItemPedido> itens = new ArrayList<>();
        for (NovoItemPedidoDTO i : dto.itens()) {
            var it = new ItemPedido();
            it.setPedido(pedido);
            it.setDescricao(i.descricao());
            it.setQuantidade(i.quantidade());
            it.setValorUnitario(i.valorUnitario());
            itens.add(it);
        }
        pedido.setItens(itens);

        // calcula total
        pedido.recalcularTotal();
        if (pedido.getTotal() == null) pedido.setTotal(BigDecimal.ZERO);

        pedidoRepository.save(pedido); // cascade persiste itens

        var body = toDTO(pedido);
        var location = uri.path("/api/v1/pedidos/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id,
                                                       @RequestBody @Valid AtualizarPedidoDTO dto) {
        var pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        // Atualiza status se vier
        if (dto.status() != null) {
            pedido.setStatus(PedidoStatus.valueOf(dto.status().name()));
        }

        // Substitui itens se vier lista
        if (dto.itens() != null) {
            // remove antigos
            pedido.getItens().clear();
            itemPedidoRepository.deleteAll(itemPedidoRepository.findByPedidoId(id));

            // adiciona novos
            List<ItemPedido> novos = new ArrayList<>();
            for (NovoItemPedidoDTO i : dto.itens()) {
                var it = new ItemPedido();
                it.setPedido(pedido);
                it.setDescricao(i.descricao());
                it.setQuantidade(i.quantidade());
                it.setValorUnitario(i.valorUnitario());
                novos.add(it);
            }
            pedido.getItens().addAll(novos);
        }

        // recalcula total
        pedido.recalcularTotal();
        if (pedido.getTotal() == null) pedido.setTotal(BigDecimal.ZERO);

        // salva
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(toDTO(pedido));
    }

    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestBody @Valid AtualizarStatusDTO dto) {
        var pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        pedido.setStatus(PedidoStatus.valueOf(dto.status().name()));
        pedidoRepository.save(pedido);
        return ResponseEntity.ok(toDTO(pedido));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        var pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        // deletar itens primeiro por segurança (mesmo com orphanRemoval=true)
        itemPedidoRepository.deleteAll(itemPedidoRepository.findByPedidoId(id));
        pedidoRepository.delete(pedido);
        return ResponseEntity.noContent().build();
    }
}
