package com.fiap.cloudcp.web;

import com.fiap.cloudcp.model.Pedido;
import com.fiap.cloudcp.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoRepository repo;
    public PedidoController(PedidoRepository repo) { this.repo = repo; }

    @GetMapping public List<Pedido> list() { return repo.findAll(); }

    @PostMapping
    public ResponseEntity<Pedido> create(@Valid @RequestBody Pedido p) {
        Pedido saved = repo.save(p);
        return ResponseEntity.created(URI.create("/api/pedidos/" + saved.getId())).body(saved);
    }
}
