package com.pet.server.requests;

import com.pet.server.validations.IsOfAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Missing first name")
    private String firstName;

    @NotBlank(message = "Missing last name")
    private String lastName;

    @NotBlank(message = "Missing email")
    @Email(message = "Not a valid email")
    private String email;

    @NotBlank(message = "Missing password")
    @Length(min = 8, message = "Password must be at least 8 symbols long")
    private String password;

    @NotNull(message = "Missing birth date")
    @IsOfAge
    private LocalDate birthDate;

}
