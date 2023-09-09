package com.azubike.ellipsis.droneapplication.medications.validation.annotations;

import com.azubike.ellipsis.droneapplication.medications.validation.validators.ImageFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
public @interface IsImageFile {
    String message() default "File must be an image (JPEG, PNG, GIF, or BMP)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
