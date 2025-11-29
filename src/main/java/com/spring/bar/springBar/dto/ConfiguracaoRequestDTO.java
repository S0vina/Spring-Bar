package com.spring.bar.springBar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ConfiguracaoRequestDTO {

    @NotNull
    @DecimalMin(value="0.0", inclusive=true, message="0 preco do couvert deve ser zero ou positivo.")
    private Double precoCouvert;

    @NotNull
    @DecimalMin(value="0.0", inclusive=true, message="0 preco do couvert deve ser zero ou positivo.")
    private Double percentualGorjetaBebidas;

    @NotNull
    @DecimalMin(value="0.0", inclusive=true, message="0 preco do couvert deve ser zero ou positivo.")
    private Double percentualGorjetaComidas;
}
