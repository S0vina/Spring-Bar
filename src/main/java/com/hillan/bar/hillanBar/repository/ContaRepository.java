package com.hillan.bar.hillanBar.repository;

import com.hillan.bar.hillanBar.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
}
