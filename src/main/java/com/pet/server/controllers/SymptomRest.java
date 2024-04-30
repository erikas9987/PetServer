package com.pet.server.controllers;

import com.pet.server.errors.SymptomNotFoundException;
import com.pet.server.model.Illness;
import com.pet.server.model.Symptom;
import com.pet.server.repos.IllnessRepository;
import com.pet.server.repos.SymptomRepository;
import com.pet.server.requests.CreateSymptomRequest;
import com.pet.server.requests.GetSymptomRequest;
import com.pet.server.seeders.SymptomSeeder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("@userAuthorizationService.isVet()")
@Validated
@RequestMapping("/symptoms")
public class SymptomRest {

    private final SymptomRepository symptomRepository;
    private final IllnessRepository illnessRepository;

    public SymptomRest(SymptomRepository symptomRepository, IllnessRepository illnessRepository) {
        this.symptomRepository = symptomRepository;
        this.illnessRepository = illnessRepository;
    }

    @GetMapping
    public List<Symptom> getAllSymptoms() {
        return symptomRepository.findAll();
    }

    @GetMapping("/detail")
    public Symptom getSymptomByName(@Valid @RequestBody GetSymptomRequest request) {
        return symptomRepository.findByName(request.getName())
                .orElseThrow(() -> new SymptomNotFoundException(request.getName()));
    }

    @PostMapping
    public Symptom createSymptom(@Valid @RequestBody CreateSymptomRequest request) {
        Symptom symptom = buildSymptomFromRequest(request);
        symptomRepository.saveAndFlush(symptom);
        return symptom;
    }

    @PostMapping("/generate")
    public List<Symptom> generateSymptoms() {
        List<Symptom> symptoms = SymptomSeeder.seedSymptoms();
        symptomRepository.saveAllAndFlush(symptoms);
        return symptoms;
    }

    @PatchMapping("/{id}")
    public Symptom updateSymptom(@PathVariable int id, @Valid @RequestBody CreateSymptomRequest request) {
        Symptom symptom = symptomRepository.findById(id)
                .orElseThrow(() -> new SymptomNotFoundException(id));
        updateSymptomFromRequest(symptom, request);
        symptomRepository.saveAndFlush(symptom);
        return symptom;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSymptom(@PathVariable int id) {
        Symptom symptom = symptomRepository.findById(id)
                .orElseThrow(() -> new SymptomNotFoundException(id));
        clearSymptomIllnesses(symptom);
        symptomRepository.delete(symptom);
    }

    private Symptom buildSymptomFromRequest(CreateSymptomRequest request) {
        List<Illness> illnesses = illnessRepository.findAllByNameIn(request.getIllnesses());
        return Symptom.builder()
                .name(request.getName())
                .illnesses(illnesses)
                .build();
    }

    private void updateSymptomFromRequest(Symptom symptom, CreateSymptomRequest request) {
        List<Illness> illnesses = illnessRepository.findAllByNameIn(request.getIllnesses());
        symptom.setName(request.getName());
        symptom.setIllnesses(illnesses);
    }

    private void clearSymptomIllnesses(Symptom symptom) {
        List<Illness> illnesses = illnessRepository.findAllBySymptomsContaining(symptom);
        illnesses.forEach(illness -> illness.getSymptoms().remove(symptom));
        illnessRepository.saveAll(illnesses);
        symptom.getIllnesses().clear();
    }

}

