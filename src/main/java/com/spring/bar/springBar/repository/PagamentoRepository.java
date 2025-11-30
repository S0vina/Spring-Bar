package com.spring.bar.springBar.repository;

import com.spring.bar.springBar.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    // Calcula o faturamento total com base na data do pagamento
    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE p.dataPagamento BETWEEN :dataInicio AND :dataFim")
    Optional<Double> sumValorByDataPagamentoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

}
