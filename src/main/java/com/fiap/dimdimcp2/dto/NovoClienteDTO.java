// src/main/java/com/fiap/dimdimcp2/dto/NovoClienteDTO.java
package com.fiap.dimdimcp2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NovoClienteDTO(
        @NotBlank String nome,
        @Email String email
) { }
