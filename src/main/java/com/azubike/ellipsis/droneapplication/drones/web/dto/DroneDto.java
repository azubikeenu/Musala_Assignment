package com.azubike.ellipsis.droneapplication.drones.web.dto;

import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
public class DroneDto {

    private Long id;
    private DroneModel model;
    private String serialNumber;
    private BigDecimal weightLimit;
    private String batteryCapacity;
    private DroneState state;
    private List<MedicationsDto> medications;
}
