package com.spring.bar.springBar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AberturaContaDTO {

    // Renomeado de 'mesaId' para 'numeroMesa' para maior clareza,
    // assumindo que é o campo usado para buscar a mesa no ContaService.
    @NotNull(message = "O número da mesa é obrigatório.")
    @Min(value = 1, message = "O número da mesa deve ser positivo.")
    private Long numeroMesa;

    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1, message = "O número de pessoas deve ser no mínimo 1.")
    private Integer numPessoas;

    // Tipo primitivo 'boolean' não pode ser nulo, então o @NotNull foi removido.
    // O valor default é 'false' caso não seja enviado no JSON (se for um campo opcional).
    // Se não for fornecido no JSON, a flag permanecerá 'false'.
    private boolean habilitarCouvert;
}