package com.pet.server.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AgeValidator implements ConstraintValidator<IsOfAge, LocalDate> {

    int minimumAge;

    @Override
    public void initialize(IsOfAge constraint) {
        this.minimumAge = constraint.minimumAge();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        LocalDate yearsAgo = today.minusYears(this.minimumAge);
        return !yearsAgo.isBefore(birthDate);
    }
}
