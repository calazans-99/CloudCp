package com.fiap.cloudcp.service;

import com.fiap.cloudcp.dto.cliente.ClienteCreateDTO;
import com.fiap.cloudcp.dto.cliente.ClienteDTO;
import java.util.List;

public interface ClienteService {
    ClienteDTO criar(ClienteCreateDTO dto);
    List<ClienteDTO> listar();
    ClienteDTO buscar(Long id);
}
