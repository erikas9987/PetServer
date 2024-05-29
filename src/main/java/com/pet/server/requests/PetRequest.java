package com.pet.server.requests;

import com.pet.server.model.Gender;
import com.pet.server.validations.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetRequest {

    @NotBlank(message = "Please specify species of your pet")
    private String species;

    @NotBlank(message = "Please specify your pet's name!")
    private String name;

    @NotBlank(message = "Please specify your pet's gender!")
    @ValidEnum(enumClass = Gender.class)
    private String gender;

    @NotNull(message = "Please specify your pet's birth date!")
    @PastOrPresent(message = "Pet cannot be born later than it is being added")
    private LocalDate birthDate;

    @NotNull(message = "Please specify your pet's height!")
    @Positive(message = "Your pet's height cannot be 0 or less!")
    private double height;

    @NotNull(message = "Please specify your pet's weight!")
    @Positive(message = "Your pet's weight cannot be 0 or less!")
    private double weight;

    public Gender getGender() {
        return Gender.valueOf(gender);
    }

}
