package com.fiap.dimdimcp2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fiap.dimdimcp2.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
