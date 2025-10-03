package com.fiap.cloudcp.web;

import com.fiap.cloudcp.model.Cliente;
import com.fiap.cloudcp.repo.ClienteRepo;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteRepo repo;
    public ClienteController(ClienteRepo repo) { this.repo = repo; }

    @GetMapping public List<Cliente> list() { return repo.findAll(); }

    @PostMapping
    public ResponseEntity<Cliente> create(@Valid @RequestBody Cliente c) {
        Cliente saved = repo.save(c);
        return ResponseEntity.created(URI.create("/api/clientes/" + saved.getId())).body(saved);
    }
}
