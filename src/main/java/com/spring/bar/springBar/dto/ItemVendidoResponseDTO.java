package com.spring.bar.springBar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemVendidoResponseDTO {
    private String  nomeProduto;
    private double valorAgregado; // Pode ser a quantidade ou a receita
    private String metricaDeCalculo; // METRICA OU QUANTIDADE

}
