package com.spring.bar.springBar.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FaturamentoResponseDTO {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private double totalFaturamento; // Soma de todos os pagamentos do periodo

}
