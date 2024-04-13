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
public class CreateIllnessRequest {

    @NotBlank(message = "Please specify the name of your illness!")
    private String name, description;
    @NotNull(message = "Please specify symptoms of your illness. Define an empty array if there are none.")
    private List<String> symptoms;

}
