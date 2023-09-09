package com.azubike.ellipsis.droneapplication.drones.web.controllers;

import com.azubike.ellipsis.droneapplication.drones.services.DroneService;
import com.azubike.ellipsis.droneapplication.drones.web.dto.CreateDroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.mappers.DroneMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
