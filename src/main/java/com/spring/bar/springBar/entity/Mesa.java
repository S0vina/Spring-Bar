package com.spring.bar.springBar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "mesas")
@Data
@NoArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private int numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMesa status = StatusMesa.LIVRE; // Livre, Aberta, Fechada

    // Relacionamento One-to-one com a Conta (A conta real)
    @OneToOne(mappedBy = "mesa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Conta comanda;

    public enum StatusMesa {
        LIVRE,
        ABERTA,
        AGUARDANDO_PAGAMENTO,
        FECHADA
    }
}
