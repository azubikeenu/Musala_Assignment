package com.azubike.ellipsis.droneapplication.drones.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoadMedicationsDto {
    private List<String> medicationCodes;
}
