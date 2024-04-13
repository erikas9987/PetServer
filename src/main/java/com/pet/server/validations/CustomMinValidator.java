package com.pet.server.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Min;

public class CustomMinValidator implements ConstraintValidator<Min, Double> {

    private double minValue;

    @Override
    public void initialize(Min constraintAnnotation) {
        this.minValue = constraintAnnotation.value();
        System.out.println(this.minValue);
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value >= minValue;
    }
}