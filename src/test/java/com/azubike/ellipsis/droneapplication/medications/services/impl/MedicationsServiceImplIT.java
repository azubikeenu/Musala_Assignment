package com.azubike.ellipsis.droneapplication.medications.services.impl;

import com.azubike.ellipsis.droneapplication.medications.repository.ImageRepository;
import com.azubike.ellipsis.droneapplication.medications.repository.MedicationRepository;
import com.azubike.ellipsis.droneapplication.medications.utils.MedicationsTestUtils;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MedicationsServiceImplIT {

    private final String FILE_PATH = "test_image.webp";
    @Autowired
    MedicationsServiceImpl medicationsService;

    @Autowired
    MedicationRepository medicationRepository;

    @Autowired
    ImageRepository imageRepository;


    @AfterEach
    void cleanDataBase() {
        medicationRepository.deleteAll();
        imageRepository.deleteAll();
    }


    @Test
    @DisplayName("It should create a new medication with default image if no image is supplied")
    @SneakyThrows
    void itShouldCreateANewMedicationWithDefaultImageIfNoImageIsSupplied() {
        final MedicationsDto medicationsDto = MedicationsTestUtils.createValidMedicationsDto(FILE_PATH);
        medicationsDto.setImageDto(null);
        final MedicationsDto returnedValue = medicationsService.createMedication(medicationsDto, null);
        assertThat(returnedValue.getImageDto()).isNotNull();
    }


    @Test
    @DisplayName("It should return a medication")
    @SneakyThrows
    void itShouldReturnAMedication()  {
        final MedicationsDto medicationsDto = MedicationsTestUtils.createValidMedicationsDto(FILE_PATH);
        final MedicationsDto medication = medicationsService.createMedication(medicationsDto, null);
        final MedicationsDto foundMed = medicationsService.findMedicationById(medication.getId());
        assertThat(foundMed.getName()).isEqualTo(medication.getName());
        assertThat(foundMed.getId()).isEqualTo(medication.getId());
        assertThat(foundMed.getCode()).isEqualTo(medication.getCode());
    }


    @Test
    @DisplayName("It should update a medication")
    @SneakyThrows
    void itShouldUpdateTheMedication()  {
        final MedicationsDto medicationsDto = MedicationsTestUtils.createValidMedicationsDto(FILE_PATH);
        final MedicationsDto medication = medicationsService.createMedication(medicationsDto, null);
        MedicationsDto foundMed = medicationsService.findMedicationById(medication.getId());
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile(
                        "imagefile", "testing.txt", "text/plain", "Azubike Richard Enu".getBytes());
        final MedicationsDto updatedValue = MedicationsDto.builder().name("updated_name")
                .weight(new BigDecimal("10.9")).code("updated_code").build();

        medicationsService.updateMedication(updatedValue, mockMultipartFile, foundMed.getId());

        foundMed = medicationsService.findMedicationById(foundMed.getId());

        assertThat(foundMed.getName()).isEqualTo(updatedValue.getName());

        assertThat(foundMed.getCode()).isEqualTo(updatedValue.getCode());

        assertThat(foundMed.getImageDto().getContent().length).isEqualTo(mockMultipartFile.getBytes().length);

    }
}
