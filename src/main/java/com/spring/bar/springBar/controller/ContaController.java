package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.dto.AberturaContaDTO;
import com.spring.bar.springBar.dto.CancelamentoDTO;
import com.spring.bar.springBar.dto.ItemPedidoRequestDTO;
import com.spring.bar.springBar.dto.PagamentoDTO;
import com.spring.bar.springBar.entity.Conta;
import com.spring.bar.springBar.entity.ItemPedido;
import com.spring.bar.springBar.entity.Pagamento;
import com.spring.bar.springBar.service.ContaService;
import com.spring.bar.springBar.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gerenciar operações relacionadas a Contas (Comandas).
 * Mapeia funções do Garçom e do Cliente.
 */
@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ItemPedidoService itemPedidoService;

    @Autowired
    private ContaService contaService;

    public ContaController(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    // Um bom ControllerAdvice capturaria as exceções e retornaria um HTTP 404 (NoSuchElementException)
    // ou 400 (IllegalArgumentException/IllegalStateException). Por simplicidade, deixamos as exceções
    // serem lançadas aqui para serem tratadas globalmente.

    // =========================================================
    // FUNÇÕES DO GARÇOM
    // =========================================================

    /**
     * [GARÇOM] Abrir mesas e associar número de pessoas. Habilitar/dispensar couvert.
     * Endpoint: POST /api/contas
     * Corpo: { "numeroMesa": 1, "numPessoas": 4, "habilitarCouvert": true }
     */
    @PostMapping
    public ResponseEntity<Conta> abrirConta(@RequestBody AberturaContaDTO dadosAbertura) {
        // Usa o DTO para receber os dados
        Conta novaConta = contaService.abrirConta(
                dadosAbertura.getNumeroMesa(),
                dadosAbertura.getNumPessoas(),
                dadosAbertura.isHabilitarCouvert()
        );
        // Retorna 201 Created com a nova conta
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    /**
     * [GARÇOM] Adicionar itens na conta da mesa.
     * Endpoint: POST /api/contas/{contaId}/pedidos?produtoId=...&quantidade=...
     */
    @PostMapping("/{contaId}/pedidos")
    public ResponseEntity<ItemPedido> adicionarPedido(@RequestBody ItemPedidoRequestDTO dto) {
        ItemPedido novoItem = ItemPedidoService.adicionarItem(
                dto.getContaId(),
                dto.getProdutoId(),
                dto.getQuantidade()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

    /**
     * [GARÇOM] Remover ou cancelar itens (com motivo).
     * Endpoint: DELETE /api/contas/{contaId}/pedidos/{itemPedidoId}
     * Corpo: { "motivo": "Produto errado" }
     */
    @DeleteMapping("/{contaId}/pedidos/{itemPedidoId}")
    public ResponseEntity<ItemPedido> cancelarItemPedido(@PathVariable int contaId,
                                                         @PathVariable long itemPedidoId,
                                                         @RequestBody CancelamentoDTO dadosCancelamento) {
        // Usa o DTO para receber o motivo
        ItemPedido itemCancelado = contaService.cancelarItemPedido(
                contaId,
                itemPedidoId,
                dadosCancelamento.getMotivo()
        );
        // Retorna 200 OK com o item marcado como cancelado.
        return ResponseEntity.ok(itemCancelado);
    }

    /**
     * [GARÇOM] Registrar pagamentos (parciais ou totais).
     * Endpoint: POST /api/contas/{contaId}/pagamentos
     * Corpo: { "valor": 50.00, "tipo": "PIX" }
     */
    @PostMapping("/{contaId}/pagamentos")
    public ResponseEntity<Pagamento> registrarPagamento(@PathVariable int contaId,
                                                        @RequestBody PagamentoDTO dadosPagamento) {
        // Usa o DTO para receber valor e tipo
        Pagamento pagamento = contaService.registrarPagamento(
                contaId,
                dadosPagamento.getValor(),
                dadosPagamento.getTipo()
        );
        return ResponseEntity.ok(pagamento);
    }

    /**
     * [GARÇOM] Fechar conta da mesa.
     * Endpoint: PUT /api/contas/{contaId}/fechar
     */
    @PutMapping("/{contaId}/fechar")
    public ResponseEntity<Void> fecharConta(@PathVariable int contaId) {
        contaService.fecharConta(contaId);
        // Retorna 200 OK (sem corpo) indicando sucesso
        return ResponseEntity.ok().build();
    }

    /**
     * [CLIENTE] Acessar consumo da própria mesa via token de acesso.
     * Endpoint: GET /api/contas/token/{tokenAcesso}/saldo
     * Retorna o valor final pendente (o saldo).
     */
    @GetMapping("/token/{tokenAcesso}/saldo")
    public ResponseEntity<Double> getSaldoPorToken(@PathVariable String tokenAcesso) {

        // Chama o novo método do Service que busca a Mesa pelo token, obtém a Conta
        // e calcula o saldo, tudo em uma única chamada.
        double saldo = contaService.calcularSaldoFinalPorToken(tokenAcesso);

        // Retorna 200 OK com o saldo calculado
        return ResponseEntity.ok(saldo);
    }

    // NOTA: Para retornar o extrato completo (itens, subtotais, gorjeta, couvert e total),
    // um endpoint adicional (ex: /token/{tokenAcesso}/extrato) e um DTO de Extrato completo
    // seriam necessários, mas o saldo já atende o requisito principal do Cliente.
}