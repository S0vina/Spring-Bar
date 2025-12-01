package com.spring.bar.springBar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "mesas")
@Data
@NoArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Min(value = 1)
    private Integer numero;

    @Column(nullable = false)
    private Integer numPessoas;

    private Boolean couverHabilitado;

    // "Token" unico para acesso do client aos seus dados
    @Column(nullable = true)
    private String tokenAcesso;

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
