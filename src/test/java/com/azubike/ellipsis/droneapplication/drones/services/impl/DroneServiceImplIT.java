package com.azubike.ellipsis.droneapplication.drones.services.impl;


import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.repository.DroneRepository;
import com.azubike.ellipsis.droneapplication.drones.services.DroneService;
import com.azubike.ellipsis.droneapplication.drones.utils.DronesTestUtils;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.LoadMedicationsDto;
import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import com.azubike.ellipsis.droneapplication.exception.NotFoundException;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.repository.ImageRepository;
import com.azubike.ellipsis.droneapplication.medications.repository.MedicationRepository;
import com.azubike.ellipsis.droneapplication.medications.utils.MedicationsTestUtils;
import com.azubike.ellipsis.droneapplication.medications.web.mappers.MedicationsMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DroneServiceImplIT {

    private final String FILE_PATH = "test_image.webp";

    @Autowired
    DroneService droneService;

    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicationRepository medicationRepository;

    @Autowired
    MedicationsMapper medicationsMapper;

    @Autowired
    ImageRepository imageRepository;


    @AfterEach
    void cleanDataBase() {
        droneRepository.deleteAll();
        medicationRepository.deleteAll();
        imageRepository.deleteAll();
    }


    @Test
    void testRegisterDrone_Success() {
        // Arrange
        final DroneDto drone = DronesTestUtils.createValidDroneDto();

        // Act
        final DroneDto registeredDrone = droneService.register(drone);

        // Assert
        assertThat(registeredDrone).isNotNull();
        assertThat(registeredDrone.getId()).isNotNull();
    }

    @Test
    void testRegisteredDrone_ConflictException() {
        // Arrange
        List<Drone> drones = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var drone = DronesTestUtils.createValidDrone();
            drone.setId(null);
            drone.setSerialNumber(drone.getSerialNumber() + i);
            drones.add(drone);
        }
        droneRepository.saveAll(drones);
        var newDrone = DronesTestUtils.createValidDroneDto();
        newDrone.setId(null);

        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.register(newDrone));


    }

    @Test
    void testFindBySerialNumber_Success() {
        // Arrange
        var drone = DronesTestUtils.createValidDrone();
        drone.setId(null);
        drone.setSerialNumber("12345");
        droneRepository.save(drone);
        // Act
        final DroneDto foundDrone = droneService.findBySerialNumber("12345");

        // Assert
        assertThat(foundDrone).isNotNull();
        assertThat(foundDrone.getSerialNumber()).isEqualTo("12345");
    }


    @Test
    void testFindBySerialNumber_NotFoundException() {
        // Act and Assert
        assertThrows(NotFoundException.class, () -> droneService.findBySerialNumber("12345"));
    }

    @Test
    @SneakyThrows
    void testLoadMedications_Success() {
        // Arrange
        final Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);
        medication.setId(null);
        medication.setCode("ABCD");
        medicationRepository.save(medication);
        var drone = DronesTestUtils.createValidDrone();
        drone.setId(null);
        drone.setSerialNumber("12345");
        droneRepository.save(drone);
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = new ArrayList<>();
        medicationCodes.add(medication.getCode());
        loadMedicationsDto.setMedicationCodes(medicationCodes);

        // Act
        final DroneDto droneDto = droneService.loadMedications(loadMedicationsDto, "12345");

        //Assert
        assertThat(droneDto).isNotNull();
        assertThat(droneDto.getMedications().size()).isEqualTo(1);

    }


    @Test
    @SneakyThrows
    void testLoadMedications_ConflictException_ForLowBatteryLevel() {
        // Arrange
        final Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);
        medication.setId(null);
        medication.setCode("ABCD");
        medicationRepository.save(medication);
        var drone = DronesTestUtils.createValidDrone();
        drone.setId(null);
        drone.setBatteryCapacity(10);
        drone.setSerialNumber("12345");
        droneRepository.save(drone);
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = new ArrayList<>();
        medicationCodes.add(medication.getCode());
        loadMedicationsDto.setMedicationCodes(medicationCodes);
        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.loadMedications(loadMedicationsDto, "12345"));

    }

    @Test
    @SneakyThrows
    void testLoadMedication_ConflictException_forLoadedState(){
        // Arrange
        final Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);
        medication.setId(null);
        medication.setCode("ABCD");
        medicationRepository.save(medication);
        var drone = DronesTestUtils.createValidDrone();
        drone.setId(null);
        drone.setState(DroneState.LOADED);
        drone.setSerialNumber("12345");
        droneRepository.save(drone);
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = new ArrayList<>();
        medicationCodes.add(medication.getCode());
        loadMedicationsDto.setMedicationCodes(medicationCodes);
        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.loadMedications(loadMedicationsDto, "12345"));

    }


    @Test
    @SneakyThrows
    void testLoadMedication_ConflictException_forWeightExceedingMaximumCapacity(){
        // Arrange
        final Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);
        medication.setId(null);
        medication.setCode("ABCD");
        medication.setWeight(new BigDecimal("60"));
        medicationRepository.save(medication);
        var drone = DronesTestUtils.createValidDrone();
        drone.setId(null);
        drone.setWeightLimit(new BigDecimal("20"));
        drone.setSerialNumber("12345");
        droneRepository.save(drone);
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = new ArrayList<>();
        medicationCodes.add(medication.getCode());
        loadMedicationsDto.setMedicationCodes(medicationCodes);
        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.loadMedications(loadMedicationsDto, "12345"));


    }

}
