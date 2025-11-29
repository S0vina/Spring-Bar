package com.spring.bar.springBar.dto;

import com.spring.bar.springBar.entity.Produto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoRequestDTO {
    @NotNull (message = "O ID do item a ser cancelado é obrigatório.")
    private String nome;

    @NotNull (message = "O ID do item a ser cancelado é obrigatório.")
    @Min(value = 1, message = "Deve haver pelo menos uma pessoa na mesa.")
    private Double preco;

    @NotNull (message = "O ID do item a ser cancelado é obrigatório.")
    private Produto.categoriaProduto categoria;


}
