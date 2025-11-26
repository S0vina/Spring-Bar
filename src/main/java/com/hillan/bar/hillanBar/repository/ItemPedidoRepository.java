package com.hillan.bar.hillanBar.repository;

import com.hillan.bar.hillanBar.entity.Conta;
import com.hillan.bar.hillanBar.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // Buscas todos os itens de uma conta especifica
    List<ItemPedido> findByConta(Conta conta);
}
