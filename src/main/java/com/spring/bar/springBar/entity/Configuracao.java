package com.spring.bar.springBar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuracoes")
@Data
@NoArgsConstructor
public class Configuracao {

    // Usaremos um ID fixo (1) para garantir que haja apenas uma linha de configuração
    @Id
    private long id = 1L;

    // Requisito: Definir preço de entrada (couvert)
    @Column(nullable = false)
    private Double precoCouvertPessoa = 1.0;

    // Requisito: Definir percentual de gorjeta para comidas
    @Column(nullable = false)
    private Double percGorjetaComida = 0.10;

    // Requisito: Definir percentual de gorjeta para bebidas
    @Column(nullable = false)
    private Double percGorjetaBebida = 0.10;
}