// src/main/java/com/fiap/dimdimcp2/repository/PedidoRepository.java
package com.fiap.dimdimcp2.repository;

import com.fiap.dimdimcp2.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> { }
