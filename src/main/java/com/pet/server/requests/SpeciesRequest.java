package com.pet.server.requests;

import com.pet.server.model.PetType;
import com.pet.server.validations.CustomMinValidator;
import com.pet.server.validations.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpeciesRequest {

    @NotBlank(message = "Please specify species' type!")
    private String name;

    @NotBlank(message = "Please specify the pet type!")
    @ValidEnum(enumClass = PetType.class)
    private String type;

    @NotNull(message = "Please specify the minimum height of species!")
    @Positive(message = "Minimum height of species cannot be 0 or less!")
    private double heightFrom;

    @NotNull(message = "Please specify the maximum height of species!")
    @Positive(message = "Maximum height of species cannot be 0 or less!")
    @Min(value = 0, message = "Maximum height cannot be less than minimum height!", groups = CustomMinValidator.class)
    private double heightTo;

    @NotNull(message = "Please specify the weight of species!")
    @Positive(message = "Weight of species cannot be 0 or less!")
    private double weight;

    @NotNull(message = "Please specify the recommended feeding rate of species!")
    @Positive(message = "Recommended feeding rate of species cannot be 0 or less!")
    private int feedingRate;

    @NotNull(message = "Please specify the color of species!")
    @Positive(message = "Color of species cannot be 0 or less!")
    private int furColor;

    @NotNull(message = "Please specify the life expectancy of species!")
    @Positive(message = "Life expectancy of species cannot be 0 or less!")
    private int lifeExpectancy;

    @NotNull(message = "Please specify the illnesses of species. Define an empty array if there are none.")
    private List<String> illnesses;

    public PetType getType() {
        return PetType.valueOf(type);
    }
}
