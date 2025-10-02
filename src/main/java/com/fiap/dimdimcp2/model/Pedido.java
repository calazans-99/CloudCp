package com.fiap.dimdimcp2.model;

import com.fiap.dimdimcp2.model.enums.PedidoStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido", schema = "dbo")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PedidoStatus status = PedidoStatus.NOVO;

    @Column(precision = 18, scale = 2)
    private BigDecimal total;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public void recalcularTotal() {
        BigDecimal t = BigDecimal.ZERO;
        for (ItemPedido it : itens) {
            BigDecimal linha = it.getValorUnitario().multiply(BigDecimal.valueOf(it.getQuantidade()));
            t = t.add(linha);
        }
        this.total = t;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public PedidoStatus getStatus() { return status; }
    public void setStatus(PedidoStatus status) { this.status = status; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(OffsetDateTime criadoEm) { this.criadoEm = criadoEm; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}
