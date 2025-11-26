package com.hillan.bar.hillanBar.entity;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;


@Entity
@Table(name = "Contas")

public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento One-to-one (A conta pertence a apenas uma mesa)
    @OneToOne
    @JoinColumn(name = "mesaId", referencedColumnName = "id", nullable = false)
    private Mesa mesa;

    // Itens pedidos (produtos)
    // Se a conta for deletada, seus pedidos sao CASCADE.ALL
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    // Pagamentos registrados
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pagamento> pagamentos;

    // Configuracoes do Bar (serao injetadas pelo Service/Config)
    private Double percGorjetaComida;
    private Double percGorjetaBebida;
    private Double perecCouvertPessoa;

    // Log de quando a conta foi aberta
    private LocalDateTime momentoAbertura;


}
