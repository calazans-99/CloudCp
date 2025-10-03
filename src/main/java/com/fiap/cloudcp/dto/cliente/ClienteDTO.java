package com.fiap.cloudcp.dto.cliente;

import java.time.OffsetDateTime;

public record ClienteDTO(
        Long id,
        String nome,
        String email,
        OffsetDateTime criadoEm
) {}
