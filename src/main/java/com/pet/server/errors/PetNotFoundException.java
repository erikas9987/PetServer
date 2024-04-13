package com.pet.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PetNotFoundException extends ResponseStatusException {

    public PetNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, "Pet not found by id " + id);
    }

}
