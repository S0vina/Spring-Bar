package com.spring.bar.springBar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO será usado no endpoint DELETE /api/contas/{contaId}/pedidos/{itemPedidoId}
@Data
@NoArgsConstructor
public class CancelamentoDTO {

    // Requisito: O Garçom deve enviar o motivo ao cancelar um item.
    @NotNull(message = "O número de pessoas é obrigatório.")
    private String motivo;

    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1)
    private Long itemPedidoID;
}