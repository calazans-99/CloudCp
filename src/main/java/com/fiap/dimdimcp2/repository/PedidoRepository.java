package com.fiap.dimdimcp2.repository;

import com.fiap.dimdimcp2.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Carrega cliente e itens junto no findAll para evitar N+1 e LazyInit no JSON
    @EntityGraph(attributePaths = {"cliente", "itens"})
    List<Pedido> findAll();
}
