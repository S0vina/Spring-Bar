package com.spring.bar.springBar.repository;

import com.spring.bar.springBar.entity.Conta;
import com.spring.bar.springBar.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // Buscas todos os itens de uma conta especifica
    List<ItemPedido> findByConta(Conta conta);

    // 1. Relatório: Itens Mais Vendidos (por Quantidade)
    @Query("SELECT ip.produto.nome, SUM(ip.quantidade) AS totalQuantidade " +
            "FROM ItemPedido ip " +
            // Filtra apenas itens não cancelados E contas abertas no período (ou ajustável para data de fechamento)
            "WHERE ip.itemCancelado = false AND ip.conta.dataAbertura BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY ip.produto.nome " +
            "ORDER BY totalQuantidade DESC")
    List<Object[]> findTopSellingItemsByQuantity(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);

    // 2. Relatório: Itens com Maior Faturamento (por Receita)
    @Query("SELECT ip.produto.nome, SUM(ip.produto.preco * ip.quantidade) AS totalReceita " +
            "FROM ItemPedido ip " +
            "WHERE ip.itemCancelado = false AND ip.conta.dataAbertura BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY ip.produto.nome " +
            "ORDER BY totalReceita DESC")
    List<Object[]> findTopSellingItemsByRevenue(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
}
