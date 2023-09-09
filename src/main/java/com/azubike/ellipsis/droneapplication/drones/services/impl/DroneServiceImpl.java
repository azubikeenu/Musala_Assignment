package com.azubike.ellipsis.droneapplication.drones.services.impl;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.repository.DroneRepository;
import com.azubike.ellipsis.droneapplication.drones.services.DroneService;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.mappers.DroneMapper;
import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DroneServiceImpl implements DroneService {
    private final DroneMapper droneMapper;
    private final DroneRepository droneRepository;

    @Override
    public DroneDto register(final DroneDto droneDto) {
        final Optional<Drone> optionalDrone = droneRepository.findBySerialNumber(droneDto.getSerialNumber());
        if (optionalDrone.isPresent())
            throw new ConflictException(String.format("Drone with serialNumber %s already exists",
                    optionalDrone.get().getSerialNumber()));
        final Drone savedDrone = droneRepository.save(droneMapper.dtoToDrone(droneDto));
        return droneMapper.droneToDto(savedDrone);
    }
}
