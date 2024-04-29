package com.pet.server;

import com.pet.server.auth.AuthenticationService;
import com.pet.server.config.JwtService;
import com.pet.server.model.User;
import com.pet.server.repos.UserRepository;
import com.pet.server.requests.RegisterRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@CucumberContextConfiguration
@SpringBootTest
public class RegistrationTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authService;

    @MockBean
    private JwtService jwtService;

    private String receivedToken;

    @Given("there is no existing user with email {string}")
    public void there_is_no_existing_user_with_email(String email) {
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
    }

    @When("a new user registers with email {string} and password {string}")
    public void a_new_user_registers_with_email_and_password(String email, String password) {
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        receivedToken = authService.register(request).getToken();
    }

    @Then("the user should be successfully registered and receive a token")
    public void the_user_should_be_successfully_registered_and_receive_a_token() {
        verify(userRepository).save(any(User.class));
        assert receivedToken.equals("mocked-jwt-token");
    }
}
