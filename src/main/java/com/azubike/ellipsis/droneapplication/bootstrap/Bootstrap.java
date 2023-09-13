package com.azubike.ellipsis.droneapplication.bootstrap;

import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.services.DroneService;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.medications.services.MedicationService;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {
    private final DroneService droneService;
    private final MedicationService medicationService;

    @Override
    public void run(final String... args) throws Exception {
        log.info("Creating seed data for medications and drones");
        createMedications();
        registerDrones();

    }


    private void createMedications() {
        MedicationsDto medOne = MedicationsDto.builder().name("amoxicillin").weight(new BigDecimal("1.5")).build();
        MedicationsDto medTwo = MedicationsDto.builder().name("doxycycline").weight(new BigDecimal("0.5")).build();
        MedicationsDto medThree = MedicationsDto.builder().name("cephalexin").weight(new BigDecimal("1")).build();
        MedicationsDto medFour = MedicationsDto.builder().name("ciprofloxacin").weight(new BigDecimal("0.8")).build();
        MedicationsDto medFive = MedicationsDto.builder().name("clindamycin").weight(new BigDecimal("3")).build();
        MedicationsDto medSix = MedicationsDto.builder().name("metronidazole").weight(new BigDecimal("5")).build();
        MedicationsDto medSeven = MedicationsDto.builder().name("azithromycin").weight(new BigDecimal("4.5")).build();
        MedicationsDto medEight = MedicationsDto.builder().name("sulfamethoxazole ").weight(new BigDecimal("3.5")).build();
        MedicationsDto medNine = MedicationsDto.builder().name("trimethoprim").weight(new BigDecimal("3.5")).build();
        MedicationsDto medTen = MedicationsDto.builder().name("benzylpenicillin ").weight(new BigDecimal("4")).build();

        List<MedicationsDto> medications = List.of(medOne, medTwo, medThree, medFour, medFive, medSix, medSeven, medEight, medNine, medTen);

        medications.forEach(medication -> {
            final MedicationsDto med = medicationService.createMedication(medication, null);
            log.debug("Successfully created medication with code {}", med.getCode());

        });
    }


    private void registerDrones() {
        DroneDto droneOne = DroneDto.builder().model(DroneModel.CRUISERWEGHT).weightLimit(new BigDecimal("25")).build();
        DroneDto droneTwo = DroneDto.builder().model(DroneModel.LIGHTWEIGHT).weightLimit(new BigDecimal("10")).build();
        DroneDto droneThree = DroneDto.builder().model(DroneModel.MIDDLEWEIGHT).weightLimit(new BigDecimal("15")).build();
        DroneDto droneFour = DroneDto.builder().model(DroneModel.HEAVYWEIGHT).weightLimit(new BigDecimal("40")).build();
        var drones = List.of(droneOne, droneTwo, droneThree, droneFour);
        drones.forEach(droneDto -> {
            final DroneDto drone = droneService.register(droneDto);
            log.debug("Successfully stored drone with serial number {}", drone.getSerialNumber());
        });
    }
}
