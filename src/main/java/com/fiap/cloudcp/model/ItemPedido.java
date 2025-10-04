package com.fiap.cloudcp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name="item_pedido", schema="dbo")
public class ItemPedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="pedido_id", nullable=false)
    private Pedido pedido;

    @Column(nullable=false, length=200)
    private String descricao;

    @Min(1)
    @Column(nullable=false)
    private Integer quantidade;

    @NotNull
    @Column(name="valor_unitario", precision=18, scale=2, nullable=false)
    private BigDecimal valorUnitario;

    public Long getId(){ return id; }
    public Pedido getPedido(){ return pedido; }
    public void setPedido(Pedido p){ this.pedido = p; }
    public String getDescricao(){ return descricao; }
    public void setDescricao(String d){ this.descricao = d; }
    public Integer getQuantidade(){ return quantidade; }
    public void setQuantidade(Integer q){ this.quantidade = q; }
    public BigDecimal getValorUnitario(){ return valorUnitario; }
    public void setValorUnitario(BigDecimal v){ this.valorUnitario = v; }
}
