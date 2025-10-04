package com.fiap.cloudcp.controller;

import com.fiap.cloudcp.dto.pedido.*;
import com.fiap.cloudcp.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService s) { this.service = s; }

    @Operation(summary = "Cria pedido (clienteId + itens)")
    @PostMapping
    public ResponseEntity<PedidoDTO> criar(@Valid @RequestBody PedidoCreateDTO dto) {
        var saved = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Lista pedidos")
    @GetMapping
    public List<PedidoDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Busca pedido por id")
    @GetMapping("/{id}")
    public PedidoDTO obter(@PathVariable Long id) {
        return service.obter(id);
    }

    @Operation(summary = "Atualiza status do pedido")
    @PatchMapping("/{id}/status")
    public PedidoDTO atualizarStatus(@PathVariable Long id, @Valid @RequestBody PedidoStatusUpdateDTO dto) {
        return service.atualizarStatus(id, dto.status());
    }

    @Operation(summary = "Adiciona item ao pedido")
    @PostMapping("/{id}/itens")
    public PedidoDTO addItem(@PathVariable Long id, @Valid @RequestBody ItemCreateDTO dto) {
        return service.adicionarItem(id, dto);
    }

    @Operation(summary = "Exclui pedido")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
