package com.pet.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FoodNotFoundException extends ResponseStatusException {

    public FoodNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, "Food not found by id " + id);
    }

    public FoodNotFoundException(String name) {
        super(HttpStatus.NOT_FOUND, "Food not found by name " + name);
    }
}
