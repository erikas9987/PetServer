package com.pet.server.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Missing email")
    @Email(message = "Not a valid email")
    private String email;

    @NotBlank(message = "Missing password")
    @Length(min = 8, message = "Password must be at least 8 symbols long")
    private String password;

}
