package com.azubike.ellipsis.droneapplication.medications.web.controllers;

import com.azubike.ellipsis.droneapplication.medications.services.MedicationService;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsPageList;
import com.azubike.ellipsis.droneapplication.medications.validation.annotations.IsImageFile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
@Validated
public class MedicationsController {
    private final MedicationService medicationService;

    @Value("${page.default.number}")
    private Integer DEFAULT_PAGE_NUMBER ;
    @Value("${page.default.size}")
    private  Integer DEFAULT_PAGE_SIZE ;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicationsPageList> getMedications(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "25") Integer pageSize
    ) {
        if (pageNumber < 0) pageNumber = DEFAULT_PAGE_NUMBER;

        if (pageSize < 1) pageNumber = DEFAULT_PAGE_SIZE;

        MedicationsPageList medications =
                medicationService.listMedications(PageRequest.of(pageNumber, pageSize));

        return ResponseEntity.ok(medications);

    }


    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<MedicationsDto> create(@Valid @ModelAttribute MedicationsDto medicationsDto
            , @RequestPart(value = "medicationsImage", required = false) @IsImageFile MultipartFile medicationsImage) {
        var returnedValue = medicationService.createMedication(medicationsDto, medicationsImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedValue);
    }


    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<MedicationsDto> findById(@PathVariable("id") Long id) {
        var returnedValue = medicationService.findMedicationById(id);
        return ResponseEntity.ok(returnedValue);
    }


    @PostMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<MedicationsDto> update(@Valid @ModelAttribute MedicationsDto medicationsDto
            , @RequestPart(value = "medicationsImage", required = false) @IsImageFile MultipartFile medicationsImage
            , @PathVariable("id") Long id) {
        var returnedValue = medicationService.updateMedication(medicationsDto, medicationsImage, id);
        return ResponseEntity.ok(returnedValue);
    }


    @DeleteMapping(value = "/{id}")
    ResponseEntity<String> delete(@PathVariable("id") Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.ok("MEDICATION SUCCESSFULLY DELETED");
    }


}
