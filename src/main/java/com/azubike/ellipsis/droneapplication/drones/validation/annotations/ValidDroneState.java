package com.azubike.ellipsis.droneapplication.drones.validation.annotations;

import com.azubike.ellipsis.droneapplication.drones.validation.validators.DroneStateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DroneStateValidator.class)
public @interface ValidDroneState {
    String message() default "Invalid drone state must be one of IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
