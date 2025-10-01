package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.model.Cliente;
import com.fiap.dimdimcp2.dto.AtualizarClienteDTO;
import com.fiap.dimdimcp2.dto.NovoClienteDTO;
import com.fiap.dimdimcp2.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteRepository clientes;

    public ClienteController(ClienteRepository clientes) {
        this.clientes = clientes;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clientes.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Cliente> criar(@RequestBody @Valid NovoClienteDTO body) {
        var c = new Cliente();
        c.setNome(body.nome());
        c.setEmail(body.email());
        var salvo = clientes.save(c);
        return ResponseEntity.created(URI.create("/api/v1/clientes/" + salvo.getId())).body(salvo);
    }

    // ====== PUT (atualizar) ======
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id,
                                             @RequestBody @Valid AtualizarClienteDTO body) {
        return clientes.findById(id)
                .map(c -> {
                    c.setNome(body.nome());
                    c.setEmail(body.email());
                    return ResponseEntity.ok(c); // gerenciado pela JPA; já será persistido ao commit
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!clientes.existsById(id)) return ResponseEntity.notFound().build();
        clientes.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
