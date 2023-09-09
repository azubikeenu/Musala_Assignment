package com.azubike.ellipsis.droneapplication.drones.validation.validators;

import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.validation.annotations.ValidDroneModel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class DroneModelValidator implements ConstraintValidator<ValidDroneModel, String> {
    @Override
    public void initialize(ValidDroneModel constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(DroneModel.values()).anyMatch(val -> value.toUpperCase().equals(val.name()));

    }
}
