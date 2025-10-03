package com.fiap.cloudcp.model;

import com.fiap.cloudcp.model.enums.PedidoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido", schema = "dbo")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PedidoStatus status;

    @NotNull
    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(name = "criado_em", nullable = false, columnDefinition = "datetimeoffset(6)")
    private OffsetDateTime criadoEm;

    @PrePersist
    void pre() {
        if (criadoEm == null) criadoEm = OffsetDateTime.now();
        if (status == null) status = PedidoStatus.NOVO;
        if (total == null) total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public void recalcularTotal() {
        var soma = itens.stream()
                .filter(i -> i.getQuantidade()!=null && i.getValorUnitario()!=null)
                .map(i -> i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        total = soma.setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public PedidoStatus getStatus() { return status; }
    public void setStatus(PedidoStatus status) { this.status = status; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(OffsetDateTime criadoEm) { this.criadoEm = criadoEm; }
}
