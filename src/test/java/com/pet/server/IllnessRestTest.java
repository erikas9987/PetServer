package com.pet.server;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pet.server.model.Illness;
import com.pet.server.model.Symptom;
import com.pet.server.repos.IllnessRepository;
import com.pet.server.repos.SymptomRepository;
import com.pet.server.requests.CreateIllnessRequest;
import com.pet.server.errors.IllnessNotFoundException;
import com.pet.server.controllers.IllnessRest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IllnessRestTest {

    @Mock
    private IllnessRepository illnessRepository;

    @Mock
    private SymptomRepository symptomRepository;

    @InjectMocks
    private IllnessRest illnessRest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllIllnesses() {
        List<Illness> expectedIllnesses = Arrays.asList(new Illness(), new Illness());
        when(illnessRepository.findAll()).thenReturn(expectedIllnesses);
        assertEquals(expectedIllnesses, illnessRest.getAllIllnesses());
    }

    @Test
    public void testCreateIllness() {
        CreateIllnessRequest request = new CreateIllnessRequest("Flu", "Influenza", Arrays.asList("Fever", "Cough"));
        List<Symptom> symptoms = Arrays.asList(new Symptom(), new Symptom());
        Illness illness = new Illness();

        when(symptomRepository.findAllByNameIn(request.getSymptoms())).thenReturn(symptoms);
        when(illnessRepository.saveAndFlush(any(Illness.class))).thenReturn(illness);

        assertEquals(illness, illnessRest.createIllness(request));
    }

    @Test
    public void testUpdateIllnessSuccess() {
        int illnessId = 1;
        CreateIllnessRequest request = new CreateIllnessRequest("Flu", "Influenza", Arrays.asList("Fever", "Cough"));
        Illness illness = new Illness();
        illness.setId(illnessId);

        when(illnessRepository.findById(illnessId)).thenReturn(Optional.of(illness));
        when(symptomRepository.findAllByNameIn(request.getSymptoms())).thenReturn(Arrays.asList(new Symptom()));
        when(illnessRepository.saveAndFlush(illness)).thenReturn(illness);

        assertEquals(illness, illnessRest.updateIllness(illnessId, request));
    }

    @Test
    public void testUpdateIllnessNotFound() {
        int illnessId = 1;
        CreateIllnessRequest request = new CreateIllnessRequest("Flu", "Influenza", Arrays.asList("Fever", "Cough"));

        when(illnessRepository.findById(illnessId)).thenReturn(Optional.empty());
        assertThrows(IllnessNotFoundException.class, () -> illnessRest.updateIllness(illnessId, request));
    }

    @Test
    public void testDeleteIllness() {
        int illnessId = 1;
        Illness illness = new Illness();
        illness.setId(illnessId);

        when(illnessRepository.findById(illnessId)).thenReturn(Optional.of(illness));
        doNothing().when(illnessRepository).delete(illness);

        assertDoesNotThrow(() -> illnessRest.deleteIllness(illnessId));
        verify(illnessRepository).delete(illness);
    }

    @Test
    public void testDeleteIllnessNotFound() {
        int illnessId = 1;
        when(illnessRepository.findById(illnessId)).thenReturn(Optional.empty());
        assertThrows(IllnessNotFoundException.class, () -> illnessRest.deleteIllness(illnessId));
    }
}
