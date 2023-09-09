package com.azubike.ellipsis.droneapplication.medications.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@ToString(exclude = "imageDto")
public class MedicationsDto {
    private Long id;

    @NotBlank(message = "medication name cannot be blank ")
    @Pattern(regexp = "^[a-zA-Z0-9\\\\\\-_]+$", message = "Name can only contain letters, numbers, '-', '_', and '\\'")
    private String name;

    @NotNull(message = "medication weight is a required field")
    @Positive(message = "medication weight must be a non-negative number")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal weight;

    @Length(min = 5 , max = 20 , message = "Number of characters for code must be a min of 5 and a max of 20")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code can only contain uppercase letters, underscores, and numbers")
    private String code;

    @JsonIgnore
    private ImageDto imageDto;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private Timestamp createdAt;

}
