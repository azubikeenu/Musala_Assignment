package com.azubike.ellipsis.droneapplication.drones.utils;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;

import java.math.BigDecimal;

public class DronesTestUtils {

    public static Drone createValidDrone() {
        return Drone.builder().id(1L).state(DroneState.IDLE).batteryCapacity(100)
                .serialNumber("ABCD").model(DroneModel.CRUISERWEGHT)
                .weightLimit(new BigDecimal("30")).build();
    } 
    
    public static DroneDto createValidDroneDto() {
        return DroneDto.builder().model(DroneModel.CRUISERWEGHT)
                .weightLimit(new BigDecimal("30")).serialNumber("ABCD").build();
    }
}
