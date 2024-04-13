package com.pet.server.controllers;

import com.pet.server.errors.IllnessNotFoundException;
import com.pet.server.model.Illness;
import com.pet.server.model.Symptom;
import com.pet.server.repos.IllnessRepository;
import com.pet.server.repos.SymptomRepository;
import com.pet.server.requests.CreateIllnessRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("@userAuthorizationService.isVet()")
public class IllnessRest {

    @Autowired
    private IllnessRepository illnessRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    @GetMapping(value = "/illnesses")
    public ResponseEntity<List<Illness>> getAllIllnesses() {
        return ResponseEntity.ok(illnessRepository.findAll());
    }

    @PostMapping(value = "/illness")
    @Validated
    public ResponseEntity<Illness> createIllness(@Valid @RequestBody CreateIllnessRequest request) {
        List<Symptom> symptoms = symptomRepository.findAllByNameIn(request.getSymptoms());
        Illness illness = Illness.builder()
                .name(request.getName())
                .description(request.getDescription())
                .symptoms(symptoms)
                .build();
        illnessRepository.saveAndFlush(illness);
        return ResponseEntity.ok(illness);
    }

    @PatchMapping(value = "/illness/{id}")
    @Validated
    public ResponseEntity<Illness> updateIllness(@PathVariable int id, @Valid @RequestBody CreateIllnessRequest request) {
        List<Symptom> symptoms = symptomRepository.findAllByNameIn(request.getSymptoms());
        Illness illness = illnessRepository.findById(id)
                .orElseThrow(() -> new IllnessNotFoundException(id));
        illness.setName(request.getName());
        illness.setDescription(request.getDescription());
        illness.setSymptoms(symptoms);
        illnessRepository.saveAndFlush(illness);
        return ResponseEntity.ok(illness);
    }

    @DeleteMapping(value = "/illness/{id}")
    public ResponseEntity<Illness> deleteIllness(@PathVariable int id) {
        Illness illness = illnessRepository.findById(id)
                .orElseThrow(() -> new IllnessNotFoundException(id));
        List<Symptom> symptoms = symptomRepository.findAllByIllnessesContaining(illness);
        for (Symptom symptom : symptoms) {
            symptom.getIllnesses().remove(illness);
        }
        illness.getSymptoms().clear();
        symptomRepository.saveAll(symptoms);
        illnessRepository.save(illness);
        illnessRepository.delete(illness);
        return ResponseEntity.ok(illness);
    }
}
