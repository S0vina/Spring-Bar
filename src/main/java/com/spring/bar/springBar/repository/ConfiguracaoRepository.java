package com.spring.bar.springBar.repository;

import com.spring.bar.springBar.entity.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Usamos Long como tipo do ID, conforme definido na Entidade (private long id = 1L)
@Repository
public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
    // Nenhuma query adicional é necessária, pois a busca e atualização será sempre pelo ID=1
}