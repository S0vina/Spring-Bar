package com.hillan.bar.hillanBar.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "produtos")

public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 70)
    private String nome;

    @Column(nullable = false)
    private Double preco;

    public enum categoriaProduto {
        COMIDA,
        BEBIDA
    }
}
