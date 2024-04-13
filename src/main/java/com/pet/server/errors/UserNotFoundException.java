package com.pet.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, "User not found by id " + id);
    }

    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "User not found by email " + email);
    }

}
