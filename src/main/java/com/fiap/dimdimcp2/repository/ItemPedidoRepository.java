package com.fiap.dimdimcp2.repository;

import com.fiap.dimdimcp2.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedidoId(Long pedidoId);
}
