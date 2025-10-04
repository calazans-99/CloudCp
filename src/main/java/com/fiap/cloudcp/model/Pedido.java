package com.fiap.cloudcp.model;

import com.fiap.cloudcp.model.enums.PedidoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido", schema = "dbo")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="cliente_id", nullable=false)
    private Cliente cliente;

    @OneToMany(mappedBy="pedido", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private PedidoStatus status;

    @NotNull
    @Column(precision=18, scale=2, nullable=false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name="criado_em", nullable=false, columnDefinition = "datetimeoffset(6)")
    private OffsetDateTime criadoEm;

    @PrePersist
    void prePersist() {
        if (status == null) status = PedidoStatus.NOVO;
        if (total == null) total = BigDecimal.ZERO;
        if (criadoEm == null) criadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void addItem(ItemPedido item){
        item.setPedido(this);
        itens.add(item);
    }

    public Long getId(){ return id; }
    public Cliente getCliente(){ return cliente; }
    public void setCliente(Cliente c){ this.cliente = c; }
    public List<ItemPedido> getItens(){ return itens; }
    public void setItens(List<ItemPedido> it){ this.itens = it; }
    public PedidoStatus getStatus(){ return status; }
    public void setStatus(PedidoStatus s){ this.status = s; }
    public BigDecimal getTotal(){ return total; }
    public void setTotal(BigDecimal t){ this.total = t; }
    public OffsetDateTime getCriadoEm(){ return criadoEm; }
    public void setCriadoEm(OffsetDateTime d){ this.criadoEm = d; }
}
