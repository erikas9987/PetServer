package com.pet.server.controllers;

import com.pet.server.errors.SymptomNotFoundException;
import com.pet.server.model.Illness;
import com.pet.server.model.Symptom;
import com.pet.server.repos.IllnessRepository;
import com.pet.server.repos.SymptomRepository;
import com.pet.server.requests.CreateSymptomRequest;
import com.pet.server.requests.GetSymptomRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@PreAuthorize("@userAuthorizationService.isVet()")
@Validated
public class SymptomRest {

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private IllnessRepository illnessRepository;

    @GetMapping(value = "/symptoms")
    public ResponseEntity<List<Symptom>> getAllSymptoms() {
        return ResponseEntity.ok(symptomRepository.findAll());
    }

    @GetMapping(value = "/symptom")
    public ResponseEntity<Symptom> getSymptomByName(@Valid @RequestBody GetSymptomRequest request) {
        Symptom symptom = symptomRepository.findByName(request.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(symptom);
    }

    @PostMapping(value = "/symptom")
    public ResponseEntity<Symptom> createSymptom(@Valid @RequestBody CreateSymptomRequest request) {
        Symptom symptom = Symptom.builder()
                .name(request.getName())
                .illnesses(illnessRepository.findAllByNameIn(request.getIllnesses()))
                .build();
        symptomRepository.saveAndFlush(symptom);
        return ResponseEntity.ok(symptom);
    }

    @PatchMapping(value = "/symptom/{id}")
    public ResponseEntity<Symptom> updateSymptom(@PathVariable int id, @Valid @RequestBody CreateSymptomRequest request) {
        Symptom symptom = symptomRepository.findById(id)
                .orElseThrow(() -> new SymptomNotFoundException(id));
        symptom.setName(request.getName());
        symptom.setIllnesses(illnessRepository.findAllByNameIn(request.getIllnesses()));
        symptomRepository.saveAndFlush(symptom);
        return ResponseEntity.ok(symptom);
    }

    @DeleteMapping(value = "/symptom/{id}")
    public ResponseEntity<Symptom> deleteSymptom(@PathVariable int id) {
        Symptom symptom = symptomRepository.findById(id)
                .orElseThrow(() -> new SymptomNotFoundException(id));
        List<Illness> illnesses = illnessRepository.findAllBySymptomsContaining(symptom);
        for (Illness illness : illnesses) {
            illness.getSymptoms().remove(symptom);
        }
        symptom.getIllnesses().clear();
        illnessRepository.saveAll(illnesses);
        symptomRepository.save(symptom);
        symptomRepository.delete(symptom);
        return ResponseEntity.ok(symptom);
    }

}
