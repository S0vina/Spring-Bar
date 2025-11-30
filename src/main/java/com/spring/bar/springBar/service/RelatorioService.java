package com.spring.bar.springBar.service;

import com.spring.bar.springBar.dto.FaturamentoResponseDTO;
import com.spring.bar.springBar.dto.ItemVendidoResponseDTO;
import com.spring.bar.springBar.repository.ItemPedidoRepository;
import com.spring.bar.springBar.repository.PagamentoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final PagamentoRepository pagamentoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public RelatorioService(PagamentoRepository pagamentoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    // --- 1. FATURAMENTO POR PERÍODO ---
    @Transactional(readOnly = true)
    public FaturamentoResponseDTO calcularFaturamento(LocalDateTime dataInicio, LocalDateTime dataFim) {

        Double total = pagamentoRepository.sumValorByDataPagamentoBetween(dataInicio, dataFim)
                .orElse(0.0);

        FaturamentoResponseDTO dto = new FaturamentoResponseDTO();
        dto.setDataInicio(dataInicio);
        dto.setDataFim(dataFim);
        dto.setTotalFaturamento(total);

        return dto;
    }

    // --- 2. ITENS MAIS VENDIDOS (por Quantidade) ---
    @Transactional(readOnly = true)
    public List<ItemVendidoResponseDTO> getTopSellingItemsByQuantity(LocalDateTime dataInicio, LocalDateTime dataFim, int limit) {

        Pageable topLimit = PageRequest.of(0, limit);
        List<Object[]> results = itemPedidoRepository.findTopSellingItemsByQuantity(dataInicio, dataFim, topLimit);

        return results.stream()
                .map(obj -> new ItemVendidoResponseDTO(
                        (String) obj[0],
                        ((Number) obj[1]).doubleValue(),
                        "QUANTIDADE"))
                .collect(Collectors.toList());
    }

    // --- 3. ITENS COM MAIOR FATURAMENTO (por Receita) ---
    @Transactional(readOnly = true)
    public List<ItemVendidoResponseDTO> getTopSellingItemsByRevenue(LocalDateTime dataInicio, LocalDateTime dataFim, int limit) {

        Pageable topLimit = PageRequest.of(0, limit);
        List<Object[]> results = itemPedidoRepository.findTopSellingItemsByRevenue(dataInicio, dataFim, topLimit);

        return results.stream()
                .map(obj -> new ItemVendidoResponseDTO(
                        (String) obj[0],
                        ((Number) obj[1]).doubleValue(),
                        "RECEITA"))
                .collect(Collectors.toList());
    }
}