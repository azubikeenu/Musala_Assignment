package com.azubike.ellipsis.droneapplication.drones.web.dto;

import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.validation.annotations.ValidDroneModel;
import com.azubike.ellipsis.droneapplication.drones.validation.annotations.ValidDroneState;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
public class DroneDto {

    private Long id;
    @ValidDroneModel
    private DroneModel model;
    @Length(max = 100, message = "serial number must have a maximum of 100 characters")
    private String serialNumber;
    @Max(value = 500, message = "weight limit must be a maximum of 500 grams")
    private BigDecimal weightLimit;
    @Positive(message = "battery capacity must be a non-zero numeric")
    private Integer batteryCapacity;
    @ValidDroneState
    private DroneState state;
    private List<MedicationsDto> medications;
}
