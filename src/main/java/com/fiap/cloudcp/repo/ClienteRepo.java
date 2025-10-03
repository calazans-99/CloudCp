package com.fiap.cloudcp.repo;

import com.fiap.cloudcp.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepo extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
