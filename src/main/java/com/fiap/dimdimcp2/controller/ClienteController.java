package com.fiap.dimdimcp2.controller;

import com.fiap.dimdimcp2.dto.AtualizarClienteDTO;
import com.fiap.dimdimcp2.dto.NovoClienteDTO;
import com.fiap.dimdimcp2.model.Cliente;
import com.fiap.dimdimcp2.repository.ClienteRepository;
import com.fiap.dimdimcp2.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteRepository clientes;
    private final PedidoRepository pedidos;

    public ClienteController(ClienteRepository clientes, PedidoRepository pedidos) {
        this.clientes = clientes;
        this.pedidos = pedidos;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clientes.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return clientes.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> criar(@RequestBody @Valid NovoClienteDTO dto,
                                   UriComponentsBuilder uri) {

        if (clientes.existsByEmailIgnoreCase(dto.email())) {
            return ResponseEntity.status(409)
                    .body(Map.of("error", "E-mail já cadastrado."));
        }

        var c = new Cliente();
        c.setNome(dto.nome());
        c.setEmail(dto.email());
        var salvo = clientes.save(c);

        var location = uri.path("/api/v1/clientes/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @RequestBody @Valid AtualizarClienteDTO dto) {
        var opt = clientes.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        var atual = opt.get();
        var novoEmail = dto.email();
        if (!atual.getEmail().equalsIgnoreCase(novoEmail)
                && clientes.existsByEmailIgnoreCase(novoEmail)) {
            return ResponseEntity.status(409)
                    .body(Map.of("error", "E-mail já cadastrado."));
        }

        atual.setNome(dto.nome());
        atual.setEmail(novoEmail);
        var salvo = clientes.save(atual);

        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (!clientes.existsById(id)) return ResponseEntity.notFound().build();

        if (pedidos.existsByClienteId(id)) {
            return ResponseEntity.status(409)
                    .body(Map.of("error", "Cliente possui pedidos e não pode ser excluído."));
        }

        clientes.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
