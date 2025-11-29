package com.spring.bar.springBar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO será usado no endpoint POST /api/contas/{contaId}/pagamentos
@Data
@NoArgsConstructor
public class PagamentoDTO {

    // Requisito: Registrar o valor do pagamento.
    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1)
    private Double valor;

    // Campo opcional para o tipo (Dinheiro, Cartão, Pix, etc.)
    @NotNull(message = "O número de pessoas é obrigatório.")
    private String tipo;
}