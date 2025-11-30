package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.dto.FaturamentoResponseDTO;
import com.spring.bar.springBar.dto.ItemVendidoResponseDTO;
import com.spring.bar.springBar.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para Relatórios (Funções do Administrador).
 */
@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    // 1. FATURAMENTO POR PERÍODO
    // Exemplo: GET /api/relatorios/faturamento?inicio=2024-01-01T00:00:00&fim=2024-01-31T23:59:59
    @GetMapping("/faturamento")
    public ResponseEntity<FaturamentoResponseDTO> getFaturamento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        FaturamentoResponseDTO relatorio = relatorioService.calcularFaturamento(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    // 2. ITENS MAIS VENDIDOS (por Quantidade)
    // Exemplo: GET /api/relatorios/mais-vendidos/quantidade?inicio=...&fim=...&limite=5
    @GetMapping("/mais-vendidos/quantidade")
    public ResponseEntity<List<ItemVendidoResponseDTO>> getItensMaisVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(defaultValue = "10") int limite) {

        List<ItemVendidoResponseDTO> relatorio = relatorioService.getTopSellingItemsByQuantity(inicio, fim, limite);
        return ResponseEntity.ok(relatorio);
    }

    // 3. ITENS COM MAIOR FATURAMENTO (por Receita)
    // Exemplo: GET /api/relatorios/mais-vendidos/receita?inicio=...&fim=...&limite=5
    @GetMapping("/mais-vendidos/receita")
    public ResponseEntity<List<ItemVendidoResponseDTO>> getItensComMaiorFaturamento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(defaultValue = "10") int limite) {

        List<ItemVendidoResponseDTO> relatorio = relatorioService.getTopSellingItemsByRevenue(inicio, fim, limite);
        return ResponseEntity.ok(relatorio);
    }
}