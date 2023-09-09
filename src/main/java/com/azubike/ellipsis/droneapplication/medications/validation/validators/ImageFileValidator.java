package com.azubike.ellipsis.droneapplication.medications.validation.validators;

import com.azubike.ellipsis.droneapplication.medications.validation.annotations.IsImageFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageFileValidator implements ConstraintValidator<IsImageFile, MultipartFile> {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp" ,"webp");

    @Override
    public void initialize(IsImageFile constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // No file to validate, consider it valid
        }
        // Get the file extension
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            // Check if the file extension is in the list of allowed image extensions
            return ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension);
        }
        return false; // Unable to determine file extension or it's not an image
    }
}
