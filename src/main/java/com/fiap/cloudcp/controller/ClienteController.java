package com.fiap.cloudcp.controller;

import com.fiap.cloudcp.dto.cliente.ClienteCreateDTO;
import com.fiap.cloudcp.dto.cliente.ClienteDTO;
import com.fiap.cloudcp.model.Cliente;
import com.fiap.cloudcp.repository.ClienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes")
public class ClienteController {

    private final ClienteRepository repo;

    public ClienteController(ClienteRepository repo) { this.repo = repo; }

    @Operation(summary = "Cria cliente")
    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteCreateDTO dto) {
        var c = new Cliente();
        c.setNome(dto.nome());
        c.setEmail(dto.email());
        c = repo.save(c);
        var out = new ClienteDTO(c.getId(), c.getNome(), c.getEmail(), c.getCriadoEm());
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @Operation(summary = "Lista clientes")
    @GetMapping
    public List<ClienteDTO> listar() {
        return repo.findAll().stream()
                .map(c -> new ClienteDTO(c.getId(), c.getNome(), c.getEmail(), c.getCriadoEm()))
                .toList();
    }
}
