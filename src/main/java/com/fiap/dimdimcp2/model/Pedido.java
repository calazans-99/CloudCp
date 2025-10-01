package com.fiap.dimdimcp2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.dimdimcp2.model.enums.PedidoStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido", schema = "dbo")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @CreationTimestamp
    @Column(name = "data_pedido", updatable = false, nullable = false)
    private OffsetDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PedidoStatus status = PedidoStatus.NOVO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    // helpers
    public void addItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
    }
    public void addItens(List<ItemPedido> itens) { itens.forEach(this::addItem); }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public OffsetDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(OffsetDateTime dataPedido) { this.dataPedido = dataPedido; }
    public PedidoStatus getStatus() { return status; }
    public void setStatus(PedidoStatus status) { this.status = status; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}
