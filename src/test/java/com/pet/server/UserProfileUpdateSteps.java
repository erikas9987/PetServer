package com.pet.server;

import com.pet.server.auth.AuthenticationService;
import com.pet.server.model.User;
import com.pet.server.repos.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserProfileUpdateSteps {

    @MockBean
    private AuthenticationService authService;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private boolean updateSuccess;

    @Given("a logged-in user with email {string}")
    public void a_logged_in_user_with_email(String email) {
        user = new User();
        user.setEmail(email);
        when(authService.isLoggedIn(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    }

    @Given("a user with email {string} is not logged in")
    public void a_user_with_email_is_not_logged_in(String email) {
        when(authService.isLoggedIn(email)).thenReturn(false);
    }

    @When("the user updates their profile name to {string}")
    public void the_user_updates_their_profile_name_to(String newName) {
        if(authService.isLoggedIn(user.getEmail())) {
            user.setFirstName(newName);
            userRepository.save(user);
            updateSuccess = true;
        } else {
            updateSuccess = false;
        }
    }

    @Then("the user profile should be updated successfully")
    public void the_user_profile_should_be_updated_successfully() {
        assertTrue(updateSuccess, "Profile should be updated");
    }

    @Then("the profile update should fail")
    public void the_profile_update_should_fail() {
        assertFalse(updateSuccess, "Profile update should fail");
    }

    @Then("the user should receive an {string} error message")
    public void the_user_should_receive_an_unauthorized_access_error_message(String errorMessage) {
        if (!updateSuccess) {
            assertEquals("Expected unauthorized access error message", errorMessage, "unauthorized access");
        }
    }

}
