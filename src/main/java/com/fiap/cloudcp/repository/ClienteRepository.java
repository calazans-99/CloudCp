package com.fiap.cloudcp.repository;

import com.fiap.cloudcp.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
