package com.pet.server.errors;

public class FoodNotFoundException extends RuntimeException {

    public FoodNotFoundException(int id) {
        super("Food not found by id " + id);
    }

}
