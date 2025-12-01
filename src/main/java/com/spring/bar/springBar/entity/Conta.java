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
public class Conta { // Corrigido o nome da classe

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento One-to-one (A conta pertence a apenas uma mesa)
    // O Cascade é importante aqui se quisermos que a conta seja salva ao salvar a mesa, mas o ContaService está salvando a Conta
    @OneToOne
    @JoinColumn(name = "numeroMesa", referencedColumnName = "id", nullable = false)
    private Mesa mesa;

    @Enumerated(EnumType.STRING)
    private StatusConta status = StatusConta.ABERTA;

    // Itens pedidos (produtos)
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    // Pagamentos registrados
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pagamento> pagamentos;

    // Configuracoes do Bar (serao injetadas pelo Service/Config)
    @Column(nullable = false)
    private Double percGorjetaComida;
    @Column(nullable = false)
    private Double percGorjetaBebida;

    @Column(nullable = false)
    private Double precoCouvertPessoa;

    // Log de quando a conta foi aberta
    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    @Column(nullable = false) // Garantindo que o número de pessoas seja obrigatório
    private Integer numeroPessoas;

    public enum StatusConta {
        ABERTA,
        FECHADA,
        PAGA,
        AGUARDANDO_PAGAMENNTO
    }

}