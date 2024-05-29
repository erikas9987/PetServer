package com.pet.server.controllers;

import com.pet.server.errors.SpeciesNotFoundException;
import com.pet.server.model.Illness;
import com.pet.server.model.Species;
import com.pet.server.repos.IllnessRepository;
import com.pet.server.repos.SpeciesRepository;
import com.pet.server.requests.SpeciesRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class SpeciesRest {

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private IllnessRepository illnessRepository;

    @GetMapping(value = "/species")
    public ResponseEntity<List<Species>> getAllSpecies() {
        return ResponseEntity.ok(speciesRepository.findAll());
    }

    @GetMapping(value = "/species/{id}")
    public ResponseEntity<Species> getSpeciesById(@PathVariable int id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        return ResponseEntity.ok(species);
    }

    @PostMapping(value = "/species")
    @PreAuthorize("@userAuthorizationService.isVet()")
    public ResponseEntity<Species> createSpecies(@Valid @RequestBody SpeciesRequest species) {
        List<Illness> illnesses = illnessRepository.findAllByNameIn(species.getIllnesses());
        Species newSpecies = Species.builder()
                .name(species.getName())
                .type(species.getType())
                .feedingRate(species.getFeedingRate())
                .furColor(species.getFurColor())
                .heightFrom(species.getHeightFrom())
                .heightTo(species.getHeightTo())
                .illnesses(illnesses)
                .lifeExpectancy(species.getLifeExpectancy())
                .weight(species.getWeight())
                .build();
        speciesRepository.saveAndFlush(newSpecies);
        return ResponseEntity.ok(newSpecies);
    }


    @PutMapping(value = "/species/{id}")
    @PreAuthorize("@userAuthorizationService.isVet()")
    public ResponseEntity<Species> updateSpecies(@PathVariable int id, @Valid @RequestBody SpeciesRequest request) {
        List<Illness> illnesses = illnessRepository.findAllByNameIn(request.getIllnesses());
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        species.setName(request.getName());
        species.setType(request.getType());
        species.setFeedingRate(request.getFeedingRate());
        species.setFurColor(request.getFurColor());
        species.setHeightFrom(request.getHeightFrom());
        species.setHeightTo(request.getHeightTo());
        species.setIllnesses(illnesses);
        species.setLifeExpectancy(request.getLifeExpectancy());
        species.setWeight(request.getWeight());
        speciesRepository.saveAndFlush(species);
        return ResponseEntity.ok(species);
    }

    @DeleteMapping(value = "/species/{id}")
    public ResponseEntity<Species> deleteSpecies(@PathVariable int id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        species.getIllnesses().clear();
        speciesRepository.save(species);
        speciesRepository.delete(species);
        return ResponseEntity.ok(species);
    }
}
