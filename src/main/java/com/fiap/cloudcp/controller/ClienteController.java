package com.fiap.cloudcp.controller;

import com.fiap.cloudcp.dto.cliente.ClienteCreateDTO;
import com.fiap.cloudcp.dto.cliente.ClienteDTO;
import com.fiap.cloudcp.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes")
public class ClienteController {

    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO criar(@Valid @RequestBody ClienteCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping
    public List<ClienteDTO> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ClienteDTO buscar(@PathVariable Long id) { return service.buscar(id); }
}
