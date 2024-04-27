package com.pet.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MaterialNotFoundException extends ResponseStatusException {

    public MaterialNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, "Material not found with id " + id);
    }

}
