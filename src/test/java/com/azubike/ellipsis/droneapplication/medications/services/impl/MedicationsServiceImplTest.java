package com.azubike.ellipsis.droneapplication.medications.services.impl;

import com.azubike.ellipsis.droneapplication.exception.NotFoundException;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.repository.MedicationRepository;
import com.azubike.ellipsis.droneapplication.medications.utils.TestUtils;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsPageList;
import com.azubike.ellipsis.droneapplication.medications.web.mappers.MedicationsMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationsServiceImplTest {
    private final String FILE_PATH = "test_image.webp";
    @Mock
    MedicationRepository medicationRepository;
    @Mock
    MedicationsMapper medicationsMapper;
    @InjectMocks
    MedicationsServiceImpl medicationsService;

    @Test
    @DisplayName("it should create a new medication")
    void itShouldCreateANewMedication() throws Exception {
        final MedicationsDto medicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);
        final Medication medication = TestUtils.createValidMedication(FILE_PATH);
        when(medicationsMapper.dtoToMedication(any(MedicationsDto.class))).thenReturn(medication);
        when(medicationsMapper.medicationsToDto(any(Medication.class))).thenReturn(medicationsDto);
        when(medicationRepository.save(any())).thenReturn(medication);
        medicationsService.createMedication(medicationsDto, null);
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }


    @Test
    @DisplayName("it should return a NotFoundException when a medication doesnt exist")
    void itShouldThrowANotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            medicationsService.findMedicationById(anyLong());
        });
    }


    @Test
    @DisplayName("It should return a MedicationDto")
    void itShouldReturnAMedicationDto() throws Exception {
        when(medicationRepository.findById(anyLong())).thenReturn(Optional.of(TestUtils.createValidMedication(FILE_PATH)));
        when(medicationsMapper.medicationsToDto(any(Medication.class))).thenReturn(TestUtils.createValidMedicationsDto(FILE_PATH));
        final MedicationsDto foundMed = medicationsService.findMedicationById(anyLong());
        assertThat(foundMed).isNotNull();

    }

    @Test
    @DisplayName("It should return a list of Medications")
    void itShouldReturnAPageableListOfMedications() throws Exception {
        List<Medication> medications = List.of(TestUtils.createValidMedication(FILE_PATH),
                TestUtils.createValidMedication(FILE_PATH));
        final MedicationsDto medicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);
        Page<Medication> medicationsPage = new PageImpl<>(medications,
                PageRequest.of(0, 10, Sort.Direction.DESC, "id"),
                medications.size());
        // Mock the behavior of the dependencies
        when(medicationRepository.findAll(any(PageRequest.class))).thenReturn(medicationsPage);
        when(medicationsMapper.medicationsToDto(any(Medication.class))).thenReturn(medicationsDto);
        MedicationsPageList result = medicationsService.listMedications(PageRequest.of(0, 10,
                Sort.Direction.DESC,
                "id"));
        assertThat(result.getTotalElements()).isEqualTo(2);

        result.getContent().forEach(content -> {
            assertThat(content).isInstanceOf(MedicationsDto.class);
        });
    }




}