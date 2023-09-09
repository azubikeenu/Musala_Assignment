package com.azubike.ellipsis.droneapplication.drones.web.dto;

import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.validation.annotations.ValidDroneModel;
import com.azubike.ellipsis.droneapplication.drones.validation.annotations.ValidDroneState;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDroneDto {

    @Length(max = 100 ,message = "serial number must have a maximum of 100 characters")
    private String serialNumber;

    @ValidDroneModel
    private String droneModel;

    @NotNull(message = "weight limit cannot be empty")
    @Max(value = 500, message = "weight limit must be a maximum of 500 grams")
    private BigDecimal weightLimit;

    @NotNull(message = "battery capacity cannot be empty")
    @Positive(message = "battery capacity must be a non-zero numeric")
    private String batteryCapacity;

    @ValidDroneState
    private String droneState;
}
