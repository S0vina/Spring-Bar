package com.spring.bar.springBar.dto;

import lombok.*;


import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ConfiguracaoRequestDTO {

    //@NotNull
    //@DecimalMin(value="0.0", inclusive=true, message="0 preco do couvert deve ser zero ou positivo.")
    private Double precocCouvert;

    private Double percentualGorjetaBebidas;

    private Double percentualGorjetacomidas;
}
