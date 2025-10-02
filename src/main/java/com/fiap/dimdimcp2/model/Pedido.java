package com.fiap.dimdimcp2.model;

import com.fiap.dimdimcp2.model.enums.PedidoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido", schema = "dbo")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- cliente ---
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // --- itens ---
    @OneToMany(mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    // --- status ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) // evita ALTER e casa com NOVO/PAGO/CANCELADO
    private PedidoStatus status;

    // --- total ---
    @NotNull
    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    // --- timestamps ---
    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    // ---------- callbacks ----------
    @PrePersist
    protected void onCreate() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
        if (status == null) {
            status = PedidoStatus.NOVO;
        }
        if (total == null) {
            total = BigDecimal.ZERO;
        }
    }

    // ---------- regras ----------
    /** Recalcula o total como soma de quantidade * valorUnitario de cada item. */
    public void recalcularTotal() {
        BigDecimal soma = BigDecimal.ZERO;
        if (itens != null) {
            for (ItemPedido i : itens) {
                if (i == null || i.getQuantidade() == null || i.getValorUnitario() == null) continue;
                BigDecimal linha = i.getValorUnitario()
                        .multiply(BigDecimal.valueOf(i.getQuantidade()))
                        .setScale(2, RoundingMode.HALF_UP);
                soma = soma.add(linha);
            }
        }
        this.total = soma.setScale(2, RoundingMode.HALF_UP);
    }

    // ---------- getters / setters ----------
    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = (itens != null) ? itens : new ArrayList<>();
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = (total != null) ? total.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    // ---------- helpers (opcionais) ----------
    public void addItem(ItemPedido item) {
        if (item == null) return;
        item.setPedido(this);
        this.itens.add(item);
    }

    public void removeItem(ItemPedido item) {
        if (item == null) return;
        this.itens.remove(item);
        item.setPedido(null);
    }
}
