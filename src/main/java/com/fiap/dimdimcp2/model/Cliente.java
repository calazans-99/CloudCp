package com.fiap.dimdimcp2.model;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    // Se você tiver o relacionamento reverso, mantenha-o ignorado no JSON
    // para evitar ciclos e payloads enormes. Descomente se existir na sua DDL.
    //
    // @OneToMany(mappedBy = "cliente")
    // @com.fasterxml.jackson.annotation.JsonIgnore
    // private java.util.List<Pedido> pedidos = new java.util.ArrayList<>();

    public Cliente() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
