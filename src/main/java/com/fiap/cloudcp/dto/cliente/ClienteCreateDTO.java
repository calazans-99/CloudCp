package com.fiap.cloudcp.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteCreateDTO(
        @NotBlank String nome,
        @Email @NotBlank String email
) {}
