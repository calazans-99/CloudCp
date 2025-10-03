package com.fiap.cloudcp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido", schema = "dbo")
public class ItemPedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String descricao;

    @NotNull @Min(1)
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(name="valor_unitario", precision = 18, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    public Long getId() {return id;}
    public Pedido getPedido() {return pedido;}
    public void setPedido(Pedido pedido) {this.pedido = pedido;}
    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public Integer getQuantidade() {return quantidade;}
    public void setQuantidade(Integer quantidade) {this.quantidade = quantidade;}
    public BigDecimal getValorUnitario() {return valorUnitario;}
    public void setValorUnitario(BigDecimal valorUnitario) {this.valorUnitario = valorUnitario;}
}
