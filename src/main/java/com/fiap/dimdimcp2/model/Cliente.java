// src/main/java/com/fiap/dimdimcp2/model/Cliente.java
package com.fiap.dimdimcp2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Entity @Table(name = "cliente")
public class Cliente {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank @Column(nullable=false, length=120)
  private String nome;

  @NotBlank @Email @Column(nullable=false, unique=true, length=120)
  private String email;

  @Column(name="criado_em", nullable=false, columnDefinition="datetimeoffset")
  private OffsetDateTime criadoEm = OffsetDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(OffsetDateTime criadoEm) { this.criadoEm = criadoEm; }
}
