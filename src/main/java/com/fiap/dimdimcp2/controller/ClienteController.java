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

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteRepository clientes;
    private final PedidoRepository pedidos;

    public ClienteController(ClienteRepository clientes, PedidoRepository pedidos) {
        this.clientes = clientes;
        this.pedidos = pedidos;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clientes.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obter(@PathVariable("id") Long id) {
        return clientes.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Cliente> atualizar(@PathVariable("id") Long id,
                                             @RequestBody @Valid AtualizarClienteDTO body) {
        return clientes.findById(id)
                .map(c -> {
                    c.setNome(body.nome());
                    c.setEmail(body.email());
                    return ResponseEntity.ok(c);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        if (!clientes.existsById(id)) return ResponseEntity.notFound().build();

        if (pedidos.existsByClienteId(id)) {
            return ResponseEntity.status(409).body(
                    Map.of("error", "Cliente possui pedidos e não pode ser excluído.")
            );
        }
        clientes.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
