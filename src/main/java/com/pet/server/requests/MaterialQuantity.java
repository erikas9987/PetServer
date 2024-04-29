package com.pet.server.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialQuantity {
    @NotNull(message = "Material ID cannot be null")
    private Long materialId;

    @NotNull(message = "Quantity of material cannot be null")
    private Double quantity;
}
