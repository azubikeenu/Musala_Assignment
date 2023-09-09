package com.azubike.ellipsis.droneapplication.medications.web.mappers;

import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.utils.TestUtils;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MedicationsMapperTest {
    @Autowired
    private MedicationsMapper medicationsMapper;
    private final String FILE_PATH = "test_image.webp";


    @Test
    @DisplayName("should convert a medication to medicationDto")
    void itShouldConvertMedicationToDto() throws IOException {
        final Medication medication = TestUtils.createValidMedication(FILE_PATH);
        final MedicationsDto medicationsDto = medicationsMapper.medicationsToDto(medication);
        assertThat(medicationsDto.getImageDto().getName()).isEqualTo(medication.getImage().getName());
    }


    @Test
    void itShouldConvertMedicationDtoToMedication() throws Exception {
        final MedicationsDto medicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);
        final Medication medication = medicationsMapper.dtoToMedication(medicationsDto);
        assertThat(medicationsDto.getImageDto().getName()).isEqualTo(medication.getImage().getName());
    }
}