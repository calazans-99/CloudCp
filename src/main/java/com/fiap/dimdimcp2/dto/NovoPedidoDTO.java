package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record NovoPedidoDTO(
        @NotNull Long clienteId,
        @NotEmpty List<NovoItemPedidoDTO> itens
) {}
