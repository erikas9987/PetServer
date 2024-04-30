package com.pet.server;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pet.server.model.Symptom;
import com.pet.server.model.Illness;
import com.pet.server.repos.SymptomRepository;
import com.pet.server.repos.IllnessRepository;
import com.pet.server.requests.CreateSymptomRequest;
import com.pet.server.requests.GetSymptomRequest;
import com.pet.server.errors.SymptomNotFoundException;
import com.pet.server.controllers.SymptomRest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SymptomRestTest {

    @Mock
    private SymptomRepository symptomRepository;

    @Mock
    private IllnessRepository illnessRepository;

    @InjectMocks
    private SymptomRest symptomRest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllSymptoms() {
        List<Symptom> expectedSymptoms = Arrays.asList(new Symptom(), new Symptom());
        when(symptomRepository.findAll()).thenReturn(expectedSymptoms);
        assertEquals(expectedSymptoms, symptomRest.getAllSymptoms());
    }

    @Test
    public void testGetSymptomByNameSuccess() {
        Symptom expectedSymptom = new Symptom();
        GetSymptomRequest request = new GetSymptomRequest("Cough");
        when(symptomRepository.findByName("Cough")).thenReturn(Optional.of(expectedSymptom));
        assertEquals(expectedSymptom, symptomRest.getSymptomByName(request));
    }

    @Test
    public void testGetSymptomByNameNotFound() {
        GetSymptomRequest request = new GetSymptomRequest("Cough");
        when(symptomRepository.findByName("Cough")).thenReturn(Optional.empty());
        assertThrows(SymptomNotFoundException.class, () -> symptomRest.getSymptomByName(request));
    }

    @Test
    public void testCreateSymptom() {
        CreateSymptomRequest request = new CreateSymptomRequest("Cough", Arrays.asList("Flu"));
        List<Illness> illnesses = Arrays.asList(new Illness());
        Symptom symptom = new Symptom();

        when(illnessRepository.findAllByNameIn(request.getIllnesses())).thenReturn(illnesses);
        when(symptomRepository.saveAndFlush(any(Symptom.class))).thenReturn(symptom);

        assertEquals(symptom, symptomRest.createSymptom(request));
    }

    @Test
    public void testGenerateSymptoms() {
        List<Symptom> symptoms = Arrays.asList(new Symptom());
        when(symptomRepository.saveAllAndFlush(anyList())).thenReturn(symptoms);
        assertEquals(symptoms, symptomRest.generateSymptoms());
    }

    @Test
    public void testUpdateSymptomSuccess() {
        int symptomId = 1;
        CreateSymptomRequest request = new CreateSymptomRequest("Cough", Arrays.asList("Flu"));
        Symptom symptom = new Symptom();
        symptom.setId(symptomId);

        when(symptomRepository.findById(symptomId)).thenReturn(Optional.of(symptom));
        when(symptomRepository.saveAndFlush(symptom)).thenReturn(symptom);

        assertEquals(symptom, symptomRest.updateSymptom(symptomId, request));
    }

    @Test
    public void testUpdateSymptomNotFound() {
        int symptomId = 1;
        CreateSymptomRequest request = new CreateSymptomRequest("Cough", Arrays.asList("Flu"));

        when(symptomRepository.findById(symptomId)).thenReturn(Optional.empty());
        assertThrows(SymptomNotFoundException.class, () -> symptomRest.updateSymptom(symptomId, request));
    }

    @Test
    public void testDeleteSymptom() {
        int symptomId = 1;
        Symptom symptom = new Symptom();
        symptom.setId(symptomId);

        when(symptomRepository.findById(symptomId)).thenReturn(Optional.of(symptom));
        doNothing().when(symptomRepository).delete(symptom);

        assertDoesNotThrow(() -> symptomRest.deleteSymptom(symptomId));
        verify(symptomRepository).delete(symptom);
    }

    @Test
    public void testDeleteSymptomNotFound() {
        int symptomId = 1;
        when(symptomRepository.findById(symptomId)).thenReturn(Optional.empty());
        assertThrows(SymptomNotFoundException.class, () -> symptomRest.deleteSymptom(symptomId));
    }
}
