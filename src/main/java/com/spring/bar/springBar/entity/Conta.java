package com.spring.bar.springBar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.util.List;
import java.time.LocalDateTime;


@Entity
@Table(name = "Contas")
@Data
@NoArgsConstructor
public class
Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento One-to-one (A conta pertence a apenas uma mesa)
    @OneToOne
    @JoinColumn(name = "mesaId", referencedColumnName = "id", nullable = false)
    private Mesa mesa;

    @Enumerated(EnumType.STRING)
    private StatusConta status = StatusConta.ABERTA;

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

    public enum StatusConta {
        ABERTA,
        FECHADA,
        PAGA
    }

}
