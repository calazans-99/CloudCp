package com.fiap.dimdimcp2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record NovoPedidoDTO(
        @NotNull Long clienteId,
        @NotNull @Size(min = 1) @Valid List<NovoItemPedidoDTO> itens
) {}
