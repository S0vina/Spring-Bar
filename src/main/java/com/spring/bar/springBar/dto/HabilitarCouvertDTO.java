package com.spring.bar.springBar.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HabilitarCouvertDTO {

    @NotNull(message = "Nao podem existir valores Nulls")
    private Boolean habilitado; // True para habilitado
}
