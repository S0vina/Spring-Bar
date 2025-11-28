package com.spring.bar.springBar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

//DTO será usado no endpoint POST /api/contas/{contaId}/pagamentos
@Data
@NoArgsConstructor
public class PagamentoDTO {

    // Requisito: Registrar o valor do pagamento.
    private Double valor;

    // Campo opcional para o tipo (Dinheiro, Cartão, Pix, etc.)
    private String tipo;
}