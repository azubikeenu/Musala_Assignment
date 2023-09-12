package com.azubike.ellipsis.droneapplication.drones.web.controllers;

import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.services.DroneService;
import com.azubike.ellipsis.droneapplication.drones.utils.DronesTestUtils;
import com.azubike.ellipsis.droneapplication.drones.web.dto.MutateDroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.LoadMedicationsDto;
import com.azubike.ellipsis.droneapplication.drones.web.mappers.DroneMapper;
import com.azubike.ellipsis.droneapplication.medications.utils.MedicationsTestUtils;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DroneController.class)
class DroneControllerTest {
    private final String BASE_URL = "/api/v1/drones";

    private final String FILE_PATH = "test_image.webp";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DroneService droneService;

    @MockBean
    DroneMapper droneMapper;


    @Test
    @SneakyThrows
    @DisplayName("it should return a status of  200 with a list of drones")
    void testGetDrones() {
        // Arrange
        List<DroneDto> droneDtos = List.of(DronesTestUtils.createValidDroneDto(), DronesTestUtils.createValidDroneDto());
        // Mock Behaviour
        when(droneService.getDrones()).thenReturn(droneDtos);
        // Act and Assert
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());
        verify(droneService, times(1)).getDrones();
    }


    @Test
    @SneakyThrows
    @DisplayName("it should return a status code of 201 when valid request body is passed")
    void testRegisterDrone_Success() {
        // Arrange
        MutateDroneDto createDroneDto = MutateDroneDto.builder().droneState("idle").droneModel("MIDDLEWEIGHT")
                .serialNumber("12345").batteryCapacity(100).weightLimit(new BigDecimal("30")).build();
        DroneDto droneDto = DronesTestUtils.createValidDroneDto();
        String payload = objectMapper.writeValueAsString(createDroneDto);
        // Mock Behaviour
        when(droneMapper.mutateDroneDtoToDroneDto(any(MutateDroneDto.class))).thenReturn(droneDto);
        when(droneService.register(any(DroneDto.class))).thenReturn(droneDto);
        // Act and Assert
        mockMvc.perform(
                        post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value(droneDto.getSerialNumber()))
                .andExpect(jsonPath("$.weightLimit").value(droneDto.getWeightLimit()));

        verify(droneMapper, times(1)).mutateDroneDtoToDroneDto(createDroneDto);
        verify(droneService, times(1)).register(droneDto);
    }


    @SneakyThrows
    @Test
    @DisplayName("it should return a status code of 400 when an invalid request body is passed")
    void testRegisterDrone_BadRequest() {
        // Arrange
        MutateDroneDto createDroneDto = MutateDroneDto.builder().droneState("idle").droneModel("MIDDLEWEIGHT")
                .serialNumber("12345").batteryCapacity(100).build();
        String payload = objectMapper.writeValueAsString(createDroneDto);

        // Act and Assert
        mockMvc.perform(
                        post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isBadRequest());

        verify(droneMapper, times(0)).mutateDroneDtoToDroneDto(createDroneDto);


    }


    @SneakyThrows
    @Test
    @DisplayName("it  should return a status code of 200 if valid list of medicationCodes is passed")
    void testLoadMedications_Success() {
        // Arrange
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        List<String> medicationCodes = List.of("12345", "6789");
        loadMedicationsDto.setMedicationCodes(medicationCodes);
        String payload = objectMapper.writeValueAsString(loadMedicationsDto);
        DroneDto droneDto = DronesTestUtils.createValidDroneDto();

        // Mock Behaviour
        when(droneService.loadMedications(any(LoadMedicationsDto.class), anyString())).thenReturn(droneDto);

        // Act and Assert
        mockMvc.perform(
                        post(String.format("%s/%s/load", BASE_URL, "12345"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isOk());

        verify(droneService, times(1)).loadMedications(loadMedicationsDto, "12345");
    }

    @Test
    @SneakyThrows
    void testLoadMedications_BadRequest() {
        // Arrange
        LoadMedicationsDto loadMedicationsDto = new LoadMedicationsDto();
        String payload = objectMapper.writeValueAsString(loadMedicationsDto);

        // Mock Behavior
        mockMvc.perform(
                        post(String.format("%s/%s/load", BASE_URL, "12345"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isBadRequest());

        // Act and Assert
        verify(droneService, times(0)).loadMedications(loadMedicationsDto, "12345");

    }


    @Test
    @SneakyThrows
    @DisplayName("it should return a status code of  200 and a list of loaded medications")
    void testGetLoadedMedications() {
        // Arrange
        List<MedicationsDto> medications = List.of(MedicationsTestUtils.createValidMedicationsDto(FILE_PATH),
                MedicationsTestUtils.createValidMedicationsDto(FILE_PATH));

        // Mock Behavior
        when(droneService.getLoadedMedications(anyString())).thenReturn(medications);

        // Act and Assert
        mockMvc.perform(get(String.format("%s/%s/loaded-medications", BASE_URL, "1234")))
                .andExpect(status().isOk());
        verify(droneService, times(1)).getLoadedMedications(anyString());

    }


    @Test
    @SneakyThrows
    @DisplayName("It should return a list of available drones")
    void testGetAvailableDrones() {
        // Arrange
        var droneOne = DronesTestUtils.createValidDroneDto();
        droneOne.setState(DroneState.IDLE);
        var droneTwo = DronesTestUtils.createValidDroneDto();
        droneTwo.setState(DroneState.LOADING);
        List<DroneDto> drones = List.of(droneOne, droneTwo);
        // Mock Behaviour
        when(droneService.getAvailableDrones()).thenReturn(drones);

        //Act and Assert
        mockMvc.perform(get(String.format("%s/available-for-loading", BASE_URL)))
                .andExpect(status().isOk());

        verify(droneService, times(1)).getAvailableDrones();
    }


    @Test
    @SneakyThrows
    @DisplayName("It should return the battery capacity of the drone")
    void testGetBatteryCapacity() {
        // Mock Behaviors
        when(droneService.getDroneBatteryCapacity(anyString())).thenReturn(100);

        // Act and Assert
        mockMvc.perform(get(String.format("%s/%s/battery-level", BASE_URL ,"12345")))
                .andExpect(status().isOk());
        verify(droneService, times(1)).getDroneBatteryCapacity(anyString());

    }


    @Test
    @SneakyThrows
    @DisplayName("It should return a status code of 200 and a drone")
    void testUpdateDrone_Success(){

        // Arrange
        var updateDroneRequest = MutateDroneDto.builder().droneState("idle").droneModel("MIDDLEWEIGHT")
                .serialNumber("12345").batteryCapacity(100).weightLimit(new BigDecimal("30")).build();
        var droneDto = DronesTestUtils.createValidDroneDto();

        var payload = objectMapper.writeValueAsString(updateDroneRequest);

        // Mock Behaviors

        when(droneMapper.mutateDroneDtoToDroneDto(any(MutateDroneDto.class))).thenReturn(droneDto);
         when(droneService.updateDrone(any(DroneDto.class) , anyString())).thenReturn(droneDto);

         // Act and Assert

        mockMvc.perform(
                        put(String.format("%s/%s" ,BASE_URL ,"12345"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value(droneDto.getSerialNumber()))
                .andExpect(jsonPath("$.weightLimit").value(droneDto.getWeightLimit()));

    }


    @Test
    public void testDeleteDrone_Success() throws Exception {
        // Arrange
        String droneSerialNumber = "123456";
        String successMessage = "DRONE WITH SERIAL NUMBER 123456 DELETED SUCCESSFULLY!";

        // Mock the behavior of droneService.deleteDrone
        when(droneService.deleteDrone(droneSerialNumber)).thenReturn(successMessage);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(String.format("%s/%s", BASE_URL, "123456"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(successMessage));
    }


}