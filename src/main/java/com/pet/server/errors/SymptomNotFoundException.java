package com.pet.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SymptomNotFoundException extends ResponseStatusException {

    public SymptomNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, "Symptom not found with id " + id);
    }

}
