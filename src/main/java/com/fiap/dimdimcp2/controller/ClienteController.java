package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.dto.NovoClienteDTO;
import com.fiap.dimdimcp2.model.Cliente;
import com.fiap.dimdimcp2.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // <--- importe aqui
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteRepository repo;

    public ClienteController(ClienteRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true) // <--- ajuste
    @GetMapping
    public List<Cliente> listar() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody @Valid NovoClienteDTO dto) {
        Cliente c = new Cliente();
        c.setNome(dto.nome());
        c.setEmail(dto.email());
        Cliente salvo = repo.save(c);
        return ResponseEntity.created(URI.create("/api/clientes/" + salvo.getId())).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obter(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
