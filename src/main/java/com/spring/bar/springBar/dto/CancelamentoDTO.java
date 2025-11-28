package com.spring.bar.springBar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

//DTO será usado no endpoint DELETE /api/contas/{contaId}/pedidos/{itemPedidoId}
@Data
@NoArgsConstructor
public class CancelamentoDTO {

    // Requisito: O Garçom deve enviar o motivo ao cancelar um item.
    private String motivo;
}