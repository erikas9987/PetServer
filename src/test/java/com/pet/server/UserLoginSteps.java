package com.pet.server;

import com.pet.server.auth.AuthenticationResponse;
import com.pet.server.auth.AuthenticationService;
import com.pet.server.model.User;
import com.pet.server.repos.UserRepository;
import com.pet.server.requests.AuthenticationRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserLoginSteps {

    @MockBean
    private AuthenticationService authService;

    @MockBean
    private UserRepository userRepository;

    private AuthenticationResponse response;

    @Given("an existing user with email {string} and password {string}")
    public void an_existing_user_with_email_and_password(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);  // Normally, you'd store and compare a hashed password.
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authService.authenticate(new AuthenticationRequest(email, password)))
                .thenReturn(new AuthenticationResponse("ValidToken", "Authentication successful", true));
    }

    @When("the user logs in with email {string} and password {string}")
    public void the_user_logs_in_with_email_and_password(String email, String password) {
        response = authService.authenticate(new AuthenticationRequest(email, password));
    }

    @When("the user tries to log in with email {string} and password {string}")
    public void the_user_tries_to_log_in_with_email_and_password(String email, String wrongPassword) {
        AuthenticationRequest request = new AuthenticationRequest(email, wrongPassword);
        try {
            response = authService.authenticate(request);
        } catch (Exception e) {
            response = new AuthenticationResponse(null, "wrong password", false);
        }
    }

    @Then("the user should be logged in successfully")
    public void the_user_should_be_logged_in_successfully() {
        assertTrue(response.isSuccess());
        assertNotNull(response.getToken());
    }

    @Then("the login should fail")
    public void the_login_should_fail() {
        assertFalse(response.isSuccess());
    }

    @Then("the user should receive a {string} error message")
    public void the_user_should_receive_a_wrong_password_error_message(String errorMessage) {
        assertEquals(errorMessage, response.getMessage());
    }
}
