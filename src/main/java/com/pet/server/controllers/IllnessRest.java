package com.pet.server.controllers;

import com.pet.server.errors.IllnessNotFoundException;
import com.pet.server.model.Illness;
import com.pet.server.model.Symptom;
import com.pet.server.repos.IllnessRepository;
import com.pet.server.repos.SymptomRepository;
import com.pet.server.requests.CreateIllnessRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("@userAuthorizationService.isVet()")
public class IllnessRest {

    private final IllnessRepository illnessRepository;
    private final SymptomRepository symptomRepository;

    public IllnessRest(IllnessRepository illnessRepository, SymptomRepository symptomRepository) {
        this.illnessRepository = illnessRepository;
        this.symptomRepository = symptomRepository;
    }

    @GetMapping("/illnesses")
    public List<Illness> getAllIllnesses() {
        return illnessRepository.findAll();
    }

    @PostMapping("/illness")
    @Validated
    public Illness createIllness(@Valid @RequestBody CreateIllnessRequest request) {
        Illness illness = buildIllnessFromRequest(request);
        illnessRepository.saveAndFlush(illness);
        return illness;
    }

    @PatchMapping("/illness/{id}")
    @Validated
    public Illness updateIllness(@PathVariable int id, @Valid @RequestBody CreateIllnessRequest request) {
        Illness illness = illnessRepository.findById(id)
                .orElseThrow(() -> new IllnessNotFoundException(id));
        updateIllnessFromRequest(illness, request);
        illnessRepository.saveAndFlush(illness);
        return illness;
    }

    @DeleteMapping("/illness/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteIllness(@PathVariable int id) {
        Illness illness = illnessRepository.findById(id)
                .orElseThrow(() -> new IllnessNotFoundException(id));
        clearIllnessSymptoms(illness);
        illnessRepository.delete(illness);
    }

    private Illness buildIllnessFromRequest(CreateIllnessRequest request) {
        List<Symptom> symptoms = symptomRepository.findAllByNameIn(request.getSymptoms());
        return Illness.builder()
                .name(request.getName())
                .description(request.getDescription())
                .symptoms(symptoms)
                .build();
    }

    private void updateIllnessFromRequest(Illness illness, CreateIllnessRequest request) {
        List<Symptom> symptoms = symptomRepository.findAllByNameIn(request.getSymptoms());
        illness.setName(request.getName());
        illness.setDescription(request.getDescription());
        illness.setSymptoms(symptoms);
    }

    private void clearIllnessSymptoms(Illness illness) {
        List<Symptom> symptoms = symptomRepository.findAllByIllnessesContaining(illness);
        symptoms.forEach(symptom -> symptom.getIllnesses().remove(illness));
        symptomRepository.saveAll(symptoms);
        illness.getSymptoms().clear();
    }
}
