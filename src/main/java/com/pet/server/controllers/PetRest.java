package com.pet.server.controllers;

import com.pet.server.errors.PetNotFoundException;
import com.pet.server.errors.SpeciesNotFoundException;
import com.pet.server.errors.UserNotFoundException;
import com.pet.server.model.Pet;
import com.pet.server.model.Species;
import com.pet.server.model.User;
import com.pet.server.repos.PetRepository;
import com.pet.server.repos.SpeciesRepository;
import com.pet.server.repos.UserRepository;
import com.pet.server.requests.PetRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class PetRest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

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

    @PostMapping(value = "/pet/{userId}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedUser(#userId)")
    public ResponseEntity<Pet> createPetForUser(@PathVariable int userId, @Valid @RequestBody PetRequest pet) {
        Species species = speciesRepository.findByName(pet.getSpecies())
                .orElseThrow(() -> new SpeciesNotFoundException(pet.getSpecies()));
        Pet newPet = Pet.builder()
                .user(userRepository.getReferenceById(userId))
                .gender(pet.getGender())
                .name(pet.getName())
                .height(pet.getHeight())
                .weight(pet.getWeight())
                .birthDate(pet.getBirthDate())
                .species(species)
                .build();
        petRepository.saveAndFlush(newPet);
        return ResponseEntity.ok(newPet);
    }

    @PutMapping(value = "/pet/{id}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedPet(#id)")
    public ResponseEntity<Pet> updatePet(@PathVariable int id, @Valid @RequestBody PetRequest request) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
        Species species = speciesRepository.findByName(request.getSpecies())
                .orElseThrow(() -> new SpeciesNotFoundException(request.getSpecies()));
        pet.setName(request.getName());
        pet.setSpecies(species);
        pet.setHeight(request.getHeight());
        pet.setWeight(request.getWeight());
        pet.setGender(request.getGender());
        pet.setBirthDate(request.getBirthDate());
        petRepository.saveAndFlush(pet);
        return ResponseEntity.ok(pet);
    }

    @DeleteMapping(value = "/pet/{id}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedPet(#id)")
    public ResponseEntity<Pet> deletePet(@PathVariable int id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
        User user = userRepository.findById(pet.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(pet.getUser().getId()));
        user.getPets().remove(pet);
        pet.setUser(null);
        userRepository.save(user);
        petRepository.save(pet);
        petRepository.delete(pet);
        return ResponseEntity.ok(pet);
    }

}
