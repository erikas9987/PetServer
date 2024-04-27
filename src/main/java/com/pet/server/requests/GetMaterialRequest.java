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
public class GetMaterialRequest {

    @NotNull(message = "Please specify the name of the material you're looking for!")
    private String name;
    private int quantity;

}
