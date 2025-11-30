package com.spring.bar.springBar.repository;

import com.spring.bar.springBar.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {

}