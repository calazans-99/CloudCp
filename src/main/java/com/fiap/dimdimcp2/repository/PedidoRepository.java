package com.fiap.dimdimcp2.repository;

import com.fiap.dimdimcp2.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    boolean existsByClienteId(Long clienteId);

    @EntityGraph(attributePaths = {"cliente","itens"})
    List<Pedido> findAll();

    @EntityGraph(attributePaths = {"cliente","itens"})
    Optional<Pedido> findWithClienteAndItensById(Long id);
}
