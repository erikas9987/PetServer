package com.pet.server.errors;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(int id) {
        super("User not found by id " + id);
    }

    public UserNotFoundException(String email) {
        super("User not found by email " + email);
    }

}
