package com.fiap.cloudcp.repo;

import com.fiap.cloudcp.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepo extends JpaRepository<Pedido, Long> {}
