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
public class CreateMaterialRequest {

    @NotBlank(message = "Please specify a name for your material!")
    private String name;
    private int quantity;

    @NotNull(message = "Please specify materials specific to your food. Define an empty array if there are none.")
    private List<String> foods;

}
