package com.spring.bar.springBar.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExtratoClienteResponseDTO {

    private String statusConta;
    private int numeroMesa;
    private int numeroPessoas;

    // Detalhes dos Itens (usaremos um DTO aninhado para o Item)
    private List<ItemExtratoDTO> itensConsumidos;

    // Valores
    private Double subtotalComida;
    private Double subtotalBebida;
    private Double totalGorjetas;
    private Double valorCouvert;
    private Double valorTotalBruto; // Total (itens + taxas)
    private Double totalPago;       // O que já foi pago
    private Double saldoPendente;   // O que falta pagar (Saldo Final)

    // DTO aninhado para evitar expor a Entidade ItemPedido/Produto
    @Data
    public static class ItemExtratoDTO {
        private String nomeProduto;
        private Double precoUnitario;
        private int quantidade;
        private Double subtotalItem;
        private boolean cancelado;
        private String motivoCancelamento; // Se cancelado
    }
}