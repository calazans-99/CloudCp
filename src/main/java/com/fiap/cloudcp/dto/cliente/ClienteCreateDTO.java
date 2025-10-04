package com.fiap.cloudcp.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteCreateDTO(
        @NotBlank @Size(max=150) String nome,
        @NotBlank @Email @Size(max=180) String email
) {}
