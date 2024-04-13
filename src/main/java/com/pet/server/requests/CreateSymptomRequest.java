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
public class CreateSymptomRequest {

    @NotBlank(message = "Please specify a name for your symptom!")
    private String name;

    @NotNull(message = "Please specify illnesses specific to your symptom. Define an empty array if there are none.")
    private List<String> illnesses;

}
