package com.azubike.ellipsis.droneapplication.drones.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class LoadMedicationsDto {
    @NotNull(message = "medicationCodes cannot be null")
    @Size(min = 1 , message = "you must add at least one medication to the drones")
    private List<String> medicationCodes;
}
