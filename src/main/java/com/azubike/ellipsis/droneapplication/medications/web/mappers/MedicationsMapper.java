package com.azubike.ellipsis.droneapplication.medications.web.mappers;

import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MedicationsMapper {
    @Autowired
    protected GenerateUniqueCode generateUniqueCode;

    @Mapping(target = "imageDto", source = "medication.image")
    public abstract MedicationsDto medicationsToDto(Medication medication);

    @Mapping(target = "image", source = "medicationsDto.imageDto")
    @Mapping(target = "code", expression = "java(generateUniqueCode.generateRandomCode(medicationsDto.getCode()))")
    public abstract Medication dtoToMedication(MedicationsDto medicationsDto);
}
