package com.spring.bar.springBar.dto;

import lombok.*;

// import org.antlr.v4.runtime.misc.NotNull; // Importação incorreta removida
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull; // Importação correta para validação

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfiguracaoRequestDTO {

    @NotNull(message = "O preço do couvert não pode ser nulo.")
    @DecimalMin(value="0.0", inclusive=true, message="O preço do couvert deve ser zero ou positivo.")
    private Double precoCouvert; // RENOMEADO: De 'precocCouvert' para 'precoCouvert'

    @NotNull(message = "O percentual da gorjeta de bebidas não pode ser nulo.")
    @DecimalMin(value="0.0", inclusive=true, message="O percentual deve ser zero ou positivo.")
    private Double percentualGorjetaBebidas;

    @NotNull(message = "O percentual da gorjeta de comidas não pode ser nulo.")
    @DecimalMin(value="0.0", inclusive=true, message="O percentual deve ser zero ou positivo.")
    private Double percentualGorjetaComidas; // RENOMEADO: De 'percentualGorjetacomidas' para 'percentualGorjetaComidas'
}