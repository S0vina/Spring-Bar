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
    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1)
    private int mesaId;

    @NotNull(message = "O número de pessoas é obrigatório.")
    @Min(value = 1)
    private int numPessoas;

    @NotNull(message = "O número de pessoas é obrigatório.")
    private boolean habilitarCouvert;
}