package com.pet.server.errors;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(int id) {
        super("Pet not found by id " + id);
    }

}
