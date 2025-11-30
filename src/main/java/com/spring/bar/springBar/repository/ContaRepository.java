package com.spring.bar.springBar.repository;

import com.spring.bar.springBar.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByTokenAcesso(String tokenAcesso);
}
