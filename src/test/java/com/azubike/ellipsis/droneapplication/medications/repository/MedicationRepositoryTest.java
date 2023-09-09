package com.azubike.ellipsis.droneapplication.medications.repository;

import com.azubike.ellipsis.droneapplication.medications.domian.Image;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MedicationRepositoryTest {
    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private ImageRepository imageRepository;
    private final String FILE_PATH = "test_image.webp";

    @Test
    @DisplayName("it should create a new medication")
    void shouldCreateNewMedication() throws Exception {
        final Medication med = TestUtils.createValidMedication(FILE_PATH);
        final Medication savedMedication = medicationRepository.save(med);
        assertThat(savedMedication).isNotNull();
    }

    @Test
    @DisplayName("It should create a new image when medication is persisted ")
    void shouldCreateNewImageWhenMedicationIsPersisted() throws Exception {
        final Medication med = TestUtils.createValidMedication(FILE_PATH);
        medicationRepository.saveAndFlush(med);
        final List<Image> allImages = imageRepository.findAll();
        assertThat(allImages.size()).isEqualTo(1);
    }

}
