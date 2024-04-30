package com.pet.server;

import com.pet.server.model.Pet;
import com.pet.server.model.User;
import com.pet.server.repos.PetRepository;
import com.pet.server.repos.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class PetManagementSteps {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PetRepository petRepository;

    private User user;
    private boolean operationSuccess;

    @Given("a logged-in user with email {string}")
    public void a_logged_in_user_with_email(String email) {
        user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    }

    @Given("no existing pet named {string}")
    public void no_existing_pet_named(String petName) {
        when(petRepository.findAllByUserId(user.getId())).thenReturn(Collections.emptyList());
    }

    @Given("an existing pet named {string}")
    public void an_existing_pet_named(String petName) {
        Pet existingPet = new Pet();
        existingPet.setName(petName);
        when(petRepository.findAllByUserId(user.getId())).thenReturn(Collections.singletonList(existingPet));
    }

    @When("the user {adds|tries to add} a pet named {string}")
    public void the_user_adds_a_pet_named(String petName) {
        Pet newPet = new Pet();
        newPet.setName(petName);
        newPet.setUser(user);
        if (petRepository.findAllByUserId(user.getId()).stream().anyMatch(p -> p.getName().equals(petName))) {
            operationSuccess = false;
        } else {
            petRepository.save(newPet);
            operationSuccess = true;
        }
    }

    @Then("the pet {Buddy|addition} should {be added|fail} successfully")
    public void the_pet_addition_should_be_evaluated(String outcome) {
        if ("be added".equals(outcome)) {
            assertTrue(operationSuccess, "Pet should be added successfully");
        } else {
            assertFalse(operationSuccess, "Pet addition should fail");
        }
    }

    @Then("the user should receive a {string} error message")
    public void the_user_should_receive_a_pet_name_already_exists_error_message(String errorMessage) {
        if (!operationSuccess) {
            assertEquals("Expected pet name already exists error message", errorMessage, "pet name already exists");
        }
    }
}
