package com.azubike.ellipsis.droneapplication.drones.services.impl;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.repository.DroneRepository;
import com.azubike.ellipsis.droneapplication.drones.utils.DronesTestUtils;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.LoadMedicationsDto;
import com.azubike.ellipsis.droneapplication.drones.web.mappers.DroneMapper;
import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import com.azubike.ellipsis.droneapplication.exception.NotFoundException;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.repository.MedicationRepository;
import com.azubike.ellipsis.droneapplication.medications.utils.MedicationsTestUtils;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {

    private final String FILE_PATH = "test_image.webp";

    @Mock
    DroneMapper droneMapper;

    @Mock
    DroneRepository droneRepository;


    @Mock
    MedicationRepository medicationRepository;

    @InjectMocks
    DroneServiceImpl droneService;


    @Test
    @DisplayName("it should register a new drone")
    void register_Success() {
        DroneDto inputDto = DronesTestUtils.createValidDroneDto();
        Drone entity = DronesTestUtils.createValidDrone();
        when(droneRepository.count()).thenReturn(9L);
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());
        when(droneMapper.dtoToDrone(inputDto)).thenReturn(entity);
        when(droneRepository.save(entity)).thenReturn(entity);
        when(droneMapper.droneToDto(entity)).thenReturn(inputDto);

        // Act
        DroneDto result = droneService.register(inputDto);

        // Assert
        assertThat(result).isNotNull();
        verify(droneRepository, times(1)).count();
        verify(droneRepository, times(1)).findBySerialNumber(anyString());
        verify(droneRepository, times(1)).save(entity);
        verify(droneMapper, times(1)).dtoToDrone(inputDto);
        verify(droneMapper, times(1)).droneToDto(entity);

    }

    @Test
    @DisplayName("it should throw a conflict exception when the maximum load capacity is reached")
    public void register_MaximumRegistrationReached() {
        // Arrange
        DroneDto inputDto = DronesTestUtils.createValidDroneDto();
        when(droneRepository.count()).thenReturn(10L);
        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.register(inputDto));
    }

    @Test
    @DisplayName("it should throw a conflict exception when the maximum load capacity is reached")
    public void testRegister_DroneAlreadyExists() {
        // Arrange
        DroneDto inputDto = DronesTestUtils.createValidDroneDto();
        when(droneRepository.count()).thenReturn(9L);
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(new Drone()));
        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.register(inputDto));
    }

    @Test
    @DisplayName("It should return a droneDto when serialNumber is valid")
    public void testFindBySerialNumber_Success() {
        // Arrange
        String serialNumber = "123456";
        Drone drone = DronesTestUtils.createValidDrone();
        DroneDto expectedDto = DronesTestUtils.createValidDroneDto();

        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(drone));
        when(droneMapper.droneToDto(drone)).thenReturn(expectedDto);
        // Act
        DroneDto result = droneService.findBySerialNumber(serialNumber);
        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("it should throw a 404 exception when serialNumber doesnt exist")
    public void testFindBySerialNumber_NotFound() {
        // Arrange
        String serialNumber = "123456";
        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(NotFoundException.class, () -> droneService.findBySerialNumber(serialNumber));
    }


    @Test
    @SneakyThrows
    @DisplayName("It should return a valid drone with LOADING state")
    public void testLoadMedications_Success_WithLoadingStatus() {
        // Arrange
        String serialNumber = "123456";
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = List.of("ABCDE", "FGHIJ");
        loadMedicationsDto.setMedicationCodes(medicationCodes);

        Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);

        Drone drone = DronesTestUtils.createValidDrone();
        DroneDto returnedValue = DronesTestUtils.createValidDroneDto();

        returnedValue.setState(DroneState.LOADING);

        List<Medication> medications = new ArrayList<>();
        drone.setMedications(medications);

        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(drone));

        when(medicationRepository.findMedicationByCode(anyString())).thenReturn(Optional.of(medication));
        when(droneMapper.dtoToDrone(any())).thenReturn(drone);
        when(droneRepository.save(drone)).thenReturn(drone);
        when(droneMapper.droneToDto(drone)).thenReturn(returnedValue);

        // Act
        DroneDto result = droneService.loadMedications(loadMedicationsDto, serialNumber);
        // Assert
        assertThat(result.getState()).isEqualTo(DroneState.LOADING); // Check the state is set as expected
    }

    @Test
    @SneakyThrows
    @DisplayName("It should throw a conflict exception when a drone is fully loaded")
    public void testLoadMedications_Conflict_WhenDroneIsAlreadyFullyLoaded() {
        // Arrange
        String serialNumber = "123456";
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = List.of("ABCDE", "FGHIJ");
        loadMedicationsDto.setMedicationCodes(medicationCodes);
        Drone drone = DronesTestUtils.createValidDrone();
        drone.setState(DroneState.LOADED);
        List<Medication> medications = new ArrayList<>();
        drone.setMedications(medications);
        Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(drone));
        when(medicationRepository.findMedicationByCode(anyString())).thenReturn(Optional.of(medication));

        when(droneMapper.dtoToDrone(any())).thenReturn(drone);

        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.loadMedications(loadMedicationsDto, serialNumber));
    }

    @Test
    @SneakyThrows
    @DisplayName("It should throw a conflict exception on attempt to load a drone with low battery")
    public void testLoadMedications_Conflict_WhenDroneBatteryIsLow() {
        // Arrange
        String serialNumber = "123456";
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = List.of("ABCDE", "FGHIJ");
        loadMedicationsDto.setMedicationCodes(medicationCodes);
        Drone drone = DronesTestUtils.createValidDrone();
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(10);
        List<Medication> medications = new ArrayList<>();
        drone.setMedications(medications);
        Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(drone));
        when(medicationRepository.findMedicationByCode(anyString())).thenReturn(Optional.of(medication));

        when(droneMapper.dtoToDrone(any())).thenReturn(drone);

        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.loadMedications(loadMedicationsDto, serialNumber));
    }

    @Test
    @SneakyThrows
    @DisplayName("It should throw a conflict exception on attempt to load a drone with medications weight greater that its weight limit")
    public void testLoadMedications_Conflict_WhenItIsLoadedAboveItsWeightLimit() {
        // Arrange
        String serialNumber = "123456";
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = List.of("ABCDE", "FGHIJ");
        loadMedicationsDto.setMedicationCodes(medicationCodes);
        Drone drone = DronesTestUtils.createValidDrone();
        List<Medication> medications = new ArrayList<>();
        drone.setMedications(medications);
        Medication medication = MedicationsTestUtils.createValidMedication(FILE_PATH);
        medication.setWeight(new BigDecimal("100"));
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(drone));
        when(medicationRepository.findMedicationByCode(anyString())).thenReturn(Optional.of(medication));

        when(droneMapper.dtoToDrone(any())).thenReturn(drone);

        // Act and Assert
        assertThrows(ConflictException.class, () -> droneService.loadMedications(loadMedicationsDto, serialNumber));
    }


    @Test
    @SneakyThrows
    public void testGetLoadedMedications_Success() {
        // Arrange
        String serialNumber = "123456";
        DroneDto droneDto = DronesTestUtils.createValidDroneDto();
        List<MedicationsDto> medications = List.of(MedicationsTestUtils.createValidMedicationsDto(FILE_PATH));
        droneDto.setMedications(medications);
        final Drone drone = DronesTestUtils.createValidDrone();
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(drone));
        when(droneMapper.droneToDto(any())).thenReturn(droneDto);

        // Act
        List<MedicationsDto> result = droneService.getLoadedMedications(serialNumber);

        // Assert
        assertThat(result).isEqualTo(medications);

    }

    @Test
    void getAvailableDrones() {

        // Arrange
        List<Drone> drones = new ArrayList<>();
        final Drone drone = DronesTestUtils.createValidDrone();
        drones.add(drone);
        final DroneDto droneDto = DronesTestUtils.createValidDroneDto();
        List<DroneDto> expectedDrones = List.of(droneDto);

        when(droneRepository.findAllByStateIn(List.of(DroneState.IDLE, DroneState.LOADING, DroneState.DELIVERED))).thenReturn(drones);
        when(droneMapper.droneToDto(any(Drone.class))).thenReturn(droneDto);

        // Act
        List<DroneDto> result = droneService.getAvailableDrones();

        // Assert
        assertThat(expectedDrones.size()).isEqualTo(result.size());
    }

    @Test
    public void testGetDroneBatteryCapacity_Success() {
        // Arrange
        String serialNumber = "123456";
        Drone drone = DronesTestUtils.createValidDrone();
        DroneDto droneDto = DronesTestUtils.createValidDroneDto();
        droneDto.setBatteryCapacity(100);
        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(drone));
        when(droneMapper.droneToDto(drone)).thenReturn(droneDto);
        // Act
        Integer result = droneService.getDroneBatteryCapacity(serialNumber);
        // Assert
        assertThat(result).isEqualTo(drone.getBatteryCapacity());
    }

    @Test
    public void testGetDroneBatteryCapacity_NotFound() {
        // Arrange
        String serialNumber = "123456";
        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(NotFoundException.class, () -> droneService.getDroneBatteryCapacity(serialNumber));
    }

    @Test
    public void testGetDrones_Success() {
        // Arrange
        List<Drone> drones = new ArrayList<>();
        drones.add(DronesTestUtils.createValidDrone());
        final DroneDto droneDto = DronesTestUtils.createValidDroneDto();
        List<DroneDto> expectedDrones = List.of(droneDto);
        when(droneRepository.findAll()).thenReturn(drones);
        when(droneMapper.droneToDto(any(Drone.class))).thenReturn(droneDto);
        // Act
        List<DroneDto> result = droneService.getDrones();
        // Assert
        assertThat(result.size()).isEqualTo(expectedDrones.size());
    }

    @Test
    public void testUpdateDrone_Success() {
        // Arrange
        String serialNumber = "123456";
        DroneDto updatedDto = DronesTestUtils.createValidDroneDto();
        Drone drone = DronesTestUtils.createValidDrone();
        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(drone));
        when(droneRepository.save(drone)).thenReturn(drone);
        when(droneMapper.droneToDto(drone)).thenReturn(updatedDto);
        // Act
        DroneDto result = droneService.updateDrone(updatedDto, serialNumber);
        // Assert
        assertThat(result).isEqualTo(updatedDto);
    }

    @Test
    public void testUpdateDrone_NotFound() {
        // Arrange
        String serialNumber = "123456";
        DroneDto updatedDto = DronesTestUtils.createValidDroneDto();
        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(NotFoundException.class, () -> droneService.updateDrone(updatedDto, serialNumber));
    }

    @Test
    public void testDeleteDrone_Success() {
        // Arrange
        String serialNumber = "123456";
        Drone existingDrone = DronesTestUtils.createValidDrone();
        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(existingDrone));
        // Act
        String result = droneService.deleteDrone(serialNumber);
        // Assert
        assertThat(result).isEqualTo("DRONE WITH SERIAL NUMBER 123456 DELETED SUCCESSFULLY!");
        verify(droneRepository, times(1)).delete(existingDrone); // Verify that delete is called once
    }

    @Test
    public void testDeleteDrone_NotFound() {
        // Arrange
        String serialNumber = "123456";
        when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(NotFoundException.class, () -> droneService.deleteDrone(serialNumber));
        verify(droneRepository, never()).delete(any()); // Verify that delete is never called
    }

}