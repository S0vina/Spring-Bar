package com.spring.bar.springBar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Data
@NoArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento Many-to-one (muitos pagamentos em uma conta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contaId")
    private Conta conta;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private String tipo; // Dinheiro, cartao, pix...

    // Registra o momento do pagamento
    private LocalDateTime dataPagamento;
}
