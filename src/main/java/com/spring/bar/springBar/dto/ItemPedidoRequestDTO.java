package com.spring.bar.springBar.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoRequestDTO {
    private Long contaId;

    private Long produtoId;

    private int quantidade;

}
