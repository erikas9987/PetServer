package com.pet.server.controllers;

import com.pet.server.model.Pet;
import com.pet.server.model.User;
import com.pet.server.repos.PetRepository;
import com.pet.server.repos.UserRepository;
import com.pet.server.utils.EmailChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@RestController
public class UserRest {

    final int DIGITAL_AGE_OF_CONSENT = 14;
    final int MIN_PASSWORD_LENGTH = 8;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/user")
    public ResponseEntity<?> getUserByEmail(@RequestBody String email) {
        if (!EmailChecker.matches(email)) {
            return new ResponseEntity<>("Bad email provided.", HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body("No email.provided");
        }
        if (!EmailChecker.matches(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Bad email provided");
        }
        if (user.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body("No password provided");
        }
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            return ResponseEntity.badRequest()
                    .body("Password must be 8 symbols or longer");
        }
        if (user.getBirthDate() == null) {
            return ResponseEntity.badRequest()
                    .body("No birth date provided");
        }
        if (Period.between(user.getBirthDate(), LocalDate.now()).getYears() < DIGITAL_AGE_OF_CONSENT) {
            return ResponseEntity.badRequest()
                    .body("User does not meet the minimum age criteria");
        }
        if (user.getFirstName() == null) {
            return ResponseEntity.badRequest()
                    .body("No first name provided");
        }
        if (user.getLastName() == null) {
            return ResponseEntity.badRequest()
                    .body("No last name provided");
        }
        User createdUser = userRepository.saveAndFlush(user);
        return ResponseEntity.ok()
                .body(createdUser);
    }

    @PatchMapping(value = "/user/{id}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedUser(#id)")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
        final String email = user.getEmail();
        if (email != null && !EmailChecker.matches(email)) {
            return ResponseEntity.badRequest()
                    .body("Bad email provided");
        }
        final String password = user.getPassword();
        if (password != null && password.length() < MIN_PASSWORD_LENGTH) {
            return ResponseEntity.badRequest()
                    .body("Password must be 8 symbols or longer");
        }
        final LocalDate birthDate = user.getBirthDate();
        if (birthDate != null && Period.between(birthDate, LocalDate.now()).getYears() < DIGITAL_AGE_OF_CONSENT) {
            return ResponseEntity.badRequest()
                    .body("User does not meet minimum age criteria");
        }
        final User foundUser = updateUser(user, optionalUser);
        User savedUser = userRepository.saveAndFlush(foundUser);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    private static User updateUser(User user, Optional<User> optionalUser) {
        User foundUser = optionalUser.get();
        if (user.getEmail() != null)
            foundUser.setEmail(user.getEmail());
        if (user.getPassword() != null)
            foundUser.setPassword(user.getPassword());
        if (user.getBirthDate() != null)
            foundUser.setBirthDate(user.getBirthDate());
        if (user.getFirstName() != null)
            foundUser.setFirstName(user.getFirstName());
        if (user.getLastName() != null)
            foundUser.setLastName(user.getLastName());
        return foundUser;
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
        User foundUser = optionalUser.get();
        final List<Pet> pets = foundUser.getPets();
        for (Pet pet : pets) {
            pet.setUser(null);
            pet.setSpecies(null);
            petRepository.delete(pet);
        }
        pets.clear();
        userRepository.delete(foundUser);
        Optional<User> userAfterDelete = userRepository.findById(id);
        if (userAfterDelete.isPresent()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user");
        }
        else {
            return ResponseEntity.ok("User deleted successfully!");
        }
    }

}
