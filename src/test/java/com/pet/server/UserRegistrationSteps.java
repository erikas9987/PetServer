package com.pet.server;

import com.pet.server.auth.AuthenticationResponse;
import com.pet.server.auth.AuthenticationService;
import com.pet.server.requests.RegisterRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRegistrationSteps {

    @MockBean
    private AuthenticationService authService;

    private AuthenticationResponse response;

    @Given("there is no existing user with email {string}")
    public void there_is_no_existing_user_with_email(String email) {
        when(authService.userExists(email)).thenReturn(false);
    }

    @When("a new user registers with email {string}, password {string}, first name {string}, last name {string}, and birth date {string}")
    public void a_new_user_registers_with_email_password_first_name_last_name_and_birth_date(
            String email, String password, String firstName, String lastName, String birthDate) {
        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(LocalDate.parse(birthDate))
                .build();
        when(authService.register(request)).thenReturn(new AuthenticationResponse("GeneratedToken", "Registration successful", true));
        response = authService.register(request);
    }

    @When("a new user attempts to register with email {string} and no password")
    public void a_new_user_attempts_to_register_with_email_and_no_password(String email) {
        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .firstName("DefaultFirstName")
                .lastName("DefaultLastName")
                .birthDate(LocalDate.now())
                .build();
        when(authService.register(request)).thenReturn(new AuthenticationResponse(null, "password is required", false));
        response = authService.register(request);
    }

    @Then("the user should be successfully registered and receive a token")
    public void the_user_should_be_successfully_registered_and_receive_a_token() {
        assertTrue(response.isSuccess(), "The registration was not successful");
        assertNotNull(response.getToken(), "The token should not be null");
    }

    @Then("the registration should fail")
    public void the_registration_should_fail() {
        assertFalse(response.isSuccess(), "The registration was unexpectedly successful");
    }

    @Then("the user should receive a {string} error message")
    public void the_user_should_receive_a_password_is_required_error_message(String errorMessage) {
        assertEquals(errorMessage, response.getMessage(), "The error message did not match the expected text");
    }


}
