package com.azubike.ellipsis.droneapplication.drones.web.controllers;

import com.azubike.ellipsis.droneapplication.drones.services.DroneService;
import com.azubike.ellipsis.droneapplication.drones.web.dto.CreateDroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.LoadMedicationsDto;
import com.azubike.ellipsis.droneapplication.drones.web.mappers.DroneMapper;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drones")
public class DroneController {
    private final DroneService droneService;
    private final DroneMapper droneMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DroneDto> register(@Valid @RequestBody CreateDroneDto createDroneDto) {
        final DroneDto droneDto = droneMapper.createDroneDtoToDroneDto(createDroneDto);
        final DroneDto returnedValue = droneService.register(droneDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedValue);
    }

    @PostMapping(value = "/{drone_serial_number}/load", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DroneDto> load(@RequestBody LoadMedicationsDto loadMedicationsDto,
                                  @PathVariable("drone_serial_number") String serialNumber) {
        return ResponseEntity.ok(droneService.loadMedications(loadMedicationsDto, serialNumber));
    }

    @GetMapping(value = "/{drone_serial_number}/loaded-medications", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<MedicationsDto>> getLoadedMedications(@PathVariable("drone_serial_number") String serialNumber) {
        return ResponseEntity.ok(droneService.getLoadedMedications(serialNumber));
    }

    @GetMapping(value = "/available-for-loading", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DroneDto>> getIdleDrones() {
        return ResponseEntity.ok(droneService.getIdleDrones());
    }

    @GetMapping(value = "/{drone_serial_number}/battery-level", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getBatteryCapacity(@PathVariable("drone_serial_number") String serialNumber) {
        final Integer droneBatteryCapacity = droneService.getDroneBatteryCapacity(serialNumber);
        return ResponseEntity.ok(String.format("The Battery capacity of the drone with serialNumber %s is %d percent", serialNumber,
                droneBatteryCapacity));
    }

}
