package com.azubike.ellipsis.droneapplication.drones.validation.validators;

import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.validation.annotations.ValidDroneState;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;


public class DroneStateValidator implements ConstraintValidator<ValidDroneState, String> {
    @Override
    public void initialize(ValidDroneState constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(DroneState.values()).anyMatch(val -> value.toUpperCase().equals(val.name()));
    }
}
