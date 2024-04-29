package com.pet.server.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodForUserRequest {

    @NotBlank(message = "Please specify the name of the food you want to add for the user.")
    private String name;

}
