package com.fiap.cloudcp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cliente", schema = "dbo")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank @Email
    @Column(nullable = false, length = 180, unique = true)
    private String email;

    @Column(name = "criado_em", nullable = false, columnDefinition = "datetimeoffset(6)")
    private OffsetDateTime criadoEm;

    @PrePersist
    void onCreate() {
        if (criadoEm == null) criadoEm = OffsetDateTime.now();
    }

    public Long getId() {return id;}
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public OffsetDateTime getCriadoEm() {return criadoEm;}
    public void setCriadoEm(OffsetDateTime criadoEm) {this.criadoEm = criadoEm;}
}
