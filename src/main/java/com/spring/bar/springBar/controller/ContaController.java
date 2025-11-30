package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.dto.*;
import com.spring.bar.springBar.entity.Conta;
import com.spring.bar.springBar.entity.ItemPedido;
import com.spring.bar.springBar.entity.Pagamento;
import com.spring.bar.springBar.service.ContaService;
import com.spring.bar.springBar.service.ItemPedidoService;
import jakarta.validation.Valid;
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
    private final ContaService contaService; // CORREÇÃO: Injeção unificada

    public ContaController(ItemPedidoService itemPedidoService, ContaService contaService) {
        this.itemPedidoService = itemPedidoService;
        this.contaService = contaService;
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
        Conta novaConta = contaService.abrirConta(dadosAbertura);
        // Retorna 201 Created com a nova conta
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    /**
     * [GARÇOM] Adicionar itens na conta da mesa.
     * Endpoint: POST /api/contas/{contaId}/pedidos?produtoId=...&quantidade=...
     */
    @PostMapping("/{contaId}/pedidos")
    public ResponseEntity<ItemPedido> adicionarPedido(@RequestBody ItemPedidoRequestDTO dto) {
        ItemPedido novoItem = itemPedidoService.adicionarItem(
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
    public ResponseEntity<ItemPedido> cancelarItemPedido(@PathVariable Long contaId,
                                                         @PathVariable long itemPedidoId,
                                                         @RequestBody CancelamentoDTO dadosCancelamento) {
        // Usa o DTO para receber o motivo
        ItemPedido itemCancelado = contaService.cancelarItemPedido(
                contaId,
                itemPedidoId,
                dadosCancelamento.getMotivo()
        );
        return ResponseEntity.ok(itemCancelado);
    }

    /**
     * [GARÇOM] Registrar pagamento na conta.
     * Endpoint: POST /api/contas/{contaId}/pagamentos
     */
    @PostMapping("/{contaId}/pagamentos")
    public ResponseEntity<Pagamento> registrarPagamento(@PathVariable Long contaId, @Valid @RequestBody PagamentoDTO dadosPagamento) {
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
    // O retorno pode ser um ResponseEntity<Conta> para mostrar o status final ou ResponseEntity<Void>
    public ResponseEntity<Conta> fecharConta(@PathVariable Long contaId) {
        // Chama o método no Service
        Conta contaFechada = contaService.fecharConta(contaId);

        // Retorna 200 OK com o objeto Conta atualizado
        return ResponseEntity.ok(contaFechada);
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

    @PutMapping("/{contaId}/couvert")
    public ResponseEntity<Conta> atualizarCouvert(@PathVariable Long contaId, @RequestBody HabilitarCouvertDTO dto){
        Conta contaAtualizada = contaService.atualizarCouvert(contaId, dto.getHabilitado());
        return ResponseEntity.ok(contaAtualizada);

    }
}