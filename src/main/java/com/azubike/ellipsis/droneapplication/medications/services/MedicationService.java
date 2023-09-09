package com.azubike.ellipsis.droneapplication.medications.services;

import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsPageList;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MedicationService {

    MedicationsDto createMedication(MedicationsDto medicationsDto, MultipartFile medicationImage);

    MedicationsDto findMedicationById(Long id);

    void deleteMedication(Long id);

    MedicationsDto findMedicationByCode(String code);

    MedicationsDto updateMedication(MedicationsDto medicationsDto, MultipartFile medicationImage, Long id);

    List<MedicationsDto> findMedicationByName(String name);

    MedicationsPageList listMedications(PageRequest pageable);


}
