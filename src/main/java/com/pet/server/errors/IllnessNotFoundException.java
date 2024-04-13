package com.pet.server.errors;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllnessNotFoundException extends ResponseStatusException {

    public IllnessNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, "Illness not found with id " + id);
    }

}
