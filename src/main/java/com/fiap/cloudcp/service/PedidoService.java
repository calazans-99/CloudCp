package com.fiap.cloudcp.service;

import com.fiap.cloudcp.dto.pedido.*;
import com.fiap.cloudcp.model.enums.PedidoStatus;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface PedidoService {
    PedidoDTO criar(PedidoCreateDTO dto);
    List<PedidoDTO> listar();
    PedidoDTO obter(Long id);
    PedidoDTO atualizarStatus(Long id, @NotNull PedidoStatus status);
    PedidoDTO adicionarItem(Long id, ItemCreateDTO dto);
    void excluir(Long id);
}
