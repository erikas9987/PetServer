package com.pet.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistsException extends ResponseStatusException {
    public UserAlreadyExistsException(String email ) {
        super(HttpStatus.BAD_REQUEST, "User already exists with email " + email);
    }
}
