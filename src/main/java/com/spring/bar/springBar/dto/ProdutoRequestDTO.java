package com.spring.bar.springBar.dto;

import com.spring.bar.springBar.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoRequestDTO {
    private String nome;
    private Double preco;
    private Produto.categoriaProduto categoria;


}
