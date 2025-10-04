package com.fiap.cloudcp.service.impl;

import com.fiap.cloudcp.dto.cliente.ClienteCreateDTO;
import com.fiap.cloudcp.dto.cliente.ClienteDTO;
import com.fiap.cloudcp.model.Cliente;
import com.fiap.cloudcp.repository.ClienteRepository;
import com.fiap.cloudcp.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    public ClienteServiceImpl(ClienteRepository repo){ this.repo = repo; }

    @Override @Transactional
    public ClienteDTO criar(ClienteCreateDTO dto) {
        var c = new Cliente();
        c.setNome(dto.nome());
        c.setEmail(dto.email());
        var saved = repo.save(c);
        return new ClienteDTO(saved.getId(), saved.getNome(), saved.getEmail(), saved.getCriadoEm());
    }

    @Override
    public List<ClienteDTO> listar() {
        return repo.findAll().stream()
                .map(c -> new ClienteDTO(c.getId(), c.getNome(), c.getEmail(), c.getCriadoEm()))
                .toList();
    }

    @Override
    public ClienteDTO buscar(Long id) {
        var c = repo.findById(id).orElseThrow();
        return new ClienteDTO(c.getId(), c.getNome(), c.getEmail(), c.getCriadoEm());
    }
}
