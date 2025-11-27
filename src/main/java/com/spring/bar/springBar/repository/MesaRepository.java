package com.spring.bar.springBar.repository;

import com.spring.bar.springBar.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository <Mesa, Long>{

    // Metodo para buscar a mesa pelo seu numero
    Optional<Mesa> findByNumero(int numero);
    Optional<Mesa> findByTokenAcesso(String token);
}
