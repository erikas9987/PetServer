package com.pet.server.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFoodRequest {

    @NotBlank(message = "Please specify the name of the food!")
    private String name;

    @NotBlank(message = "Please specify the manufacturer of the food!")
    private String manufacturer;

    @NotNull(message = "Please specify the weight of the food!")
    private double weight;

    @NotNull(message = "Please specify materials of the food. Define an empty array if there are none.")
    private List<MaterialQuantity> materials;

}

