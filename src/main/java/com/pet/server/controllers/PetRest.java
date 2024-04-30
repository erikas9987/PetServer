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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@Validated
@RequestMapping("/pets")
public class PetRest {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final SpeciesRepository speciesRepository;

    public PetRest(PetRepository petRepository, UserRepository userRepository, SpeciesRepository speciesRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.speciesRepository = speciesRepository;
    }

    @GetMapping
    public List<Pet> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        if (pets.isEmpty()) {
            throw new PetNotFoundException(0);
        }
        return pets;
    }

    @GetMapping("/{id}")
    public Pet getPetById(@PathVariable int id) {
        return petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedUser(#userId)")
    public List<Pet> getPetsByUser(@PathVariable int userId) {
        List<Pet> pets = petRepository.findAllByUserId(userId);
        if (pets.isEmpty()) {
            throw new PetNotFoundException(userId);
        }
        return pets;
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedUser(#userId)")
    public Pet createPetForUser(@PathVariable int userId, @Valid @RequestBody PetRequest petRequest) {
        Species species = speciesRepository.findByName(petRequest.getSpecies())
                .orElseThrow(() -> new SpeciesNotFoundException(petRequest.getSpecies()));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Pet newPet = buildPet(petRequest, species, user);
        return petRepository.save(newPet);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedPet(#id)")
    public Pet updatePet(@PathVariable int id, @Valid @RequestBody PetRequest petRequest) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        updatePetFromRequest(pet, petRequest);
        return petRepository.save(pet);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@userAuthorizationService.isAuthorizedPet(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable int id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        pet.setUser(null);
        petRepository.delete(pet);
    }

    private Pet buildPet(PetRequest petRequest, Species species, User user) {
        return Pet.builder()
                .user(user)
                .gender(petRequest.getGender())
                .name(petRequest.getName())
                .height(petRequest.getHeight())
                .weight(petRequest.getWeight())
                .birthDate(petRequest.getBirthDate())
                .species(species)
                .build();
    }

    private void updatePetFromRequest(Pet pet, PetRequest petRequest) {
        pet.setName(petRequest.getName());
        pet.setSpecies(speciesRepository.findByName(petRequest.getSpecies())
                .orElseThrow(() -> new SpeciesNotFoundException(petRequest.getSpecies())));
        pet.setHeight(petRequest.getHeight());
        pet.setWeight(petRequest.getWeight());
        pet.setGender(petRequest.getGender());
        pet.setBirthDate(petRequest.getBirthDate());
    }
}

