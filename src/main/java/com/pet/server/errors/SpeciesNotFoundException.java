package com.pet.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SpeciesNotFoundException extends ResponseStatusException {

    public SpeciesNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, "Species not found by id " + id);
    }

    public SpeciesNotFoundException(String name) {
        super(HttpStatus.NOT_FOUND, "Species not found by name " + name);
    }
}
