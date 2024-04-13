package com.pet.server.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface IsOfAge {
    int minimumAge() default 14;
    String message() default "User must be at least {minimumAge} years old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
