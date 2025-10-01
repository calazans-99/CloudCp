package com.fiap.dimdimcp2.repository;

import com.fiap.dimdimcp2.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Override
    @EntityGraph(attributePaths = {"cliente", "itens"})
    List<Pedido> findAll();
}
