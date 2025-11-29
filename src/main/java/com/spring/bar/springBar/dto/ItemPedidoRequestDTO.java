package com.spring.bar.springBar.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoRequestDTO {
    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1)
    private Long contaId;

    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1)
    private Long produtoId;

    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1)
    private int quantidade;

}
