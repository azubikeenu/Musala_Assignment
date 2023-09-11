package com.azubike.ellipsis.droneapplication.drones.services.impl;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.repository.DroneRepository;
import com.azubike.ellipsis.droneapplication.drones.services.DroneService;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.LoadMedicationsDto;
import com.azubike.ellipsis.droneapplication.drones.web.mappers.DroneMapper;
import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import com.azubike.ellipsis.droneapplication.exception.NotFoundException;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.repository.MedicationRepository;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DroneServiceImpl implements DroneService {
    private final DroneMapper droneMapper;
    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;


    @Override
    public DroneDto register(final DroneDto droneDto) {
        final long count = droneRepository.count();
        if (count >= 10) {
            throw new ConflictException("Maximum amount of drone registration reached");
        }
        final Optional<Drone> optionalDrone = droneRepository.findBySerialNumber(droneDto.getSerialNumber());
        if (optionalDrone.isPresent())
            throw new ConflictException(String.format("Drone with serialNumber %s already exists",
                    optionalDrone.get().getSerialNumber()));
        final Drone savedDrone = droneRepository.save(droneMapper.dtoToDrone(droneDto));
        return droneMapper.droneToDto(savedDrone);
    }


    @Override
    public DroneDto findBySerialNumber(final String serialNumber) {
        final Drone drone = droneRepository.findBySerialNumber(serialNumber).orElseThrow(() ->
                new NotFoundException(String.format("drone with serial number %s does not exist", serialNumber)));
        return droneMapper.droneToDto(drone);
    }

    @Override
    public DroneDto loadMedications(final LoadMedicationsDto loadMedicationsDto, String serialNumber) {
        var drone = droneMapper.dtoToDrone(this.findBySerialNumber(serialNumber));
        var medications = this.getMedications(loadMedicationsDto);
        drone.getMedications().addAll(medications);
        if (isDroneOkToLoad(drone)) {
            if (drone.getWeightLimit().compareTo(getTotalWeight(drone)) == 0)
                drone.setState(DroneState.LOADED);
            else {
                drone.setState(DroneState.LOADING);
            }
            final Drone savedDrone = droneRepository.save(drone);
            return droneMapper.droneToDto(savedDrone);
        }
        throw new ConflictException("Drone cannot be loaded");
    }

    @Override
    public List<MedicationsDto> getLoadedMedications(final String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new
                NotFoundException(String.format("drone with serialNumber %s does not exist", serialNumber)));
        return droneMapper.droneToDto(drone).getMedications();
    }

    @Override
    public List<DroneDto> getAvailableDrones() {
        List<DroneDto> idleDrones = new ArrayList<>();
        final List<Drone> drones = droneRepository.findAllByStateIn(List.of(DroneState.IDLE, DroneState.LOADING, DroneState.DELIVERED));
        for (Drone drone : drones) {
            idleDrones.add(droneMapper.droneToDto(drone));
        }
        return idleDrones;
    }

    @Override
    public Integer getDroneBatteryCapacity(final String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new
                NotFoundException(String.format("drone with serialNumber %s does not exist", serialNumber)));
        return droneMapper.droneToDto(drone).getBatteryCapacity();
    }

    @Override
    public List<DroneDto> getDrones() {
        List<DroneDto> droneDtoList = new ArrayList<>();
        List<Drone> drones = droneRepository.findAll();
        for (Drone drone : drones) {
            droneDtoList.add(droneMapper.droneToDto(drone));
        }

        return droneDtoList;
    }

    @Override
    public DroneDto updateDrone(DroneDto droneDto, String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new
                NotFoundException(String.format("drone with serialNumber %s does not exist", serialNumber)));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(drone, droneDto);
        final Drone updatedDrone = droneRepository.save(drone);
        return droneMapper.droneToDto(updatedDrone);
    }

    @Override
    public String deleteDrone(final String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new
                NotFoundException(String.format("drone with serialNumber %s does not exist", serialNumber)));
        droneRepository.delete(drone);
        return String.format("DRONE WITH SERIAL NUMBER %s DELETED SUCCESSFULLY!", serialNumber);
    }


    private List<Medication> getMedications(final LoadMedicationsDto loadMedicationsDto) {
        return loadMedicationsDto.getMedicationCodes().stream().parallel().map(code ->
                medicationRepository.findMedicationByCode(code).orElseThrow(() -> new NotFoundException(String.format(
                        "Medication with code %s does not exist ", code)))).toList();
    }


    private boolean isDroneFullyLoaded(Drone drone) {
        return drone.getState().equals(DroneState.LOADED);
    }


    private boolean hasLowBattery(Drone drone) {
        return drone.getBatteryCapacity() < 25;
    }

    private boolean isWithinWeightCapacity(Drone drone) {
        return drone.getWeightLimit().compareTo(getTotalWeight(drone)) >= 0;
    }

    private BigDecimal getTotalWeight(Drone drone) {
        return drone.getMedications().stream().parallel()
                .map(Medication::getWeight).reduce(BigDecimal::add).orElse(new BigDecimal('0'));
    }


    private boolean isDroneOkToLoad(Drone drone) {

        if (isDroneFullyLoaded(drone)) throw new ConflictException("Drone has reached its maximum capacity");

        else if (hasLowBattery(drone))
            throw new ConflictException(String.format("Drone's battery level %s percent is too low",
                    drone.getBatteryCapacity()));

        else if (!isWithinWeightCapacity(drone))
            throw new ConflictException(String.format("Drone's weight limit is %s gram(s) and cannot accommodate " +
                            "an extra weight of %s gram(s)"
                    , drone.getWeightLimit(), getTotalWeight(drone).subtract(drone.getWeightLimit())));

        return true;
    }

}
