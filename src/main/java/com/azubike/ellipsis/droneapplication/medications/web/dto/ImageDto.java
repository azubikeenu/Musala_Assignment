package com.azubike.ellipsis.droneapplication.medications.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {

    private Integer id;
    @NotBlank(message = "image name is a required field")
    private String name;

    @Positive(message = "image size should be a non-negative number")
    private Long size;

    @NotNull(message = "image content is required")
    private byte[] content;
}
