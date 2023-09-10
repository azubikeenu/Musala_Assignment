package com.azubike.ellipsis.droneapplication.drones.services;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.LoadMedicationsDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;

import java.util.List;

public interface DroneService {
    DroneDto register(DroneDto droneDto);

    DroneDto findBySerialNumber(String serialNumber);

    DroneDto loadMedications(LoadMedicationsDto loadMedicationsDto, String serialNumber);

    List<MedicationsDto> getLoadedMedications(String serialNumber);

    List<DroneDto> getIdleDrones();

    Integer  getDroneBatteryCapacity(String serialNumber);


}

