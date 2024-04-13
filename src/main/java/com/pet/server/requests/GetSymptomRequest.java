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
public class GetSymptomRequest {

    @NotNull(message = "Please specify the name of the symptom you're looking for!")
    private String name;

}
