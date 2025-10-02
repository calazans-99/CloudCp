package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarClienteDTO(
        @NotBlank @Size(max=150) String nome,
        @NotBlank @Email @Size(max=180) String email
) {}
