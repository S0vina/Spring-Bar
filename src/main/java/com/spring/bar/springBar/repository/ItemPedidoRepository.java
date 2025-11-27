package com.spring.bar.springBar.repository;

import com.spring.bar.springBar.entity.Conta;
import com.spring.bar.springBar.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // Buscas todos os itens de uma conta especifica
    List<ItemPedido> findByConta(Conta conta);
}
