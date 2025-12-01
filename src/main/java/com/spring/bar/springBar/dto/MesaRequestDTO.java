package com.spring.bar.springBar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO (Data Transfer Object) para receber dados na criação e edição de Mesas.
 *
 * NOTA: Esta classe utiliza Lombok para gerar automaticamente:
 * - Getters e Setters (@Data)
 * - Construtor sem argumentos (@NoArgsConstructor)
 * - Construtor com todos os argumentos (@AllArgsConstructor)
 * * Se o compilador estava reclamando de construtor duplicado, era porque
 * o construtor sem argumentos estava sendo declarado manualmente e pelo @NoArgsConstructor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaRequestDTO {

    @Min(value = 0, message = "O número de pessoas não pode ser negativo.")
    private Integer Numero;

    private Integer numPessoas;

    // O status e o couverHabilitado podem ser gerenciados pelo Service
    // Mas incluí campos opcionais para maior flexibilidade na API.
    private Boolean couverHabilitado;
}