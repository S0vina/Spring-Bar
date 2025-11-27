package com.spring.bar.springBar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "itensPedidos")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento Many-to-one (muitos itens podem ser pedidos em uma conta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contaId")
    private Conta conta;

    // Relacionamento many-to-one (muitos itens pedidos podem ser do mesmo produto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtoId")
    private Produto produto;

    @Column(nullable = false)
    private int quantidade;

    // private String motivoCancelamento (cliente pode remover itemPedido com motivo)
    private String motivoCancelamento;

    private Boolean itemCancelado = false;

}
