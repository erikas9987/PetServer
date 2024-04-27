package com.pet.server.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFoodRequest {

    @NotBlank(message = "Please specify the name of the food!")
    private String name, manufacturer;
    private double weight;
    @NotNull(message = "Please specify materials of the food. Define an empty array if there are none.")
    private List<String> materials;

}
