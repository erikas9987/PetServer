package com.pet.server.controllers;

import com.pet.server.model.Pet;
import com.pet.server.repos.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PetRest {

    @Autowired
    private PetRepository petRepository;

    @GetMapping(value = "/pets")
    public ResponseEntity<?> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(pets, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/pet/{id}")
    public ResponseEntity<?> getPetById(@PathVariable int id) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if (optionalPet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(optionalPet.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/pets/{userId}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedUser(#userId)")
    public ResponseEntity<?> getPetsByUser(@PathVariable int userId) {
        List<Pet> pets = petRepository.findAllByUserId(userId);
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(pets, HttpStatus.OK);
        }
    }

    
}
