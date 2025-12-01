package com.spring.bar.springBar.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 70)
    private String nome;

    @Column(nullable = false)
    private Double preco;

    @Enumerated(EnumType.STRING) // Garante que o nome do ENUM ('COMIDA' ou 'BEBIDA') seja salvo como string
    @Column(nullable = false)
    private categoriaProduto categoria;

    public enum categoriaProduto {
        COMIDA,
        BEBIDA
    }
}
