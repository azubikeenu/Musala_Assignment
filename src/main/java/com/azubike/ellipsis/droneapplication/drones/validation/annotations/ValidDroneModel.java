package com.azubike.ellipsis.droneapplication.drones.validation.annotations;

import com.azubike.ellipsis.droneapplication.drones.validation.validators.DroneModelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DroneModelValidator.class)
public @interface ValidDroneModel {
    String message() default "Invalid drone model must me one of LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEGHT, HEAVYWEIGHT ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
