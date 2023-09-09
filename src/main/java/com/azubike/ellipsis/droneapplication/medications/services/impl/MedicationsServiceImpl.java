package com.azubike.ellipsis.droneapplication.medications.services.impl;

import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import com.azubike.ellipsis.droneapplication.exception.ImageProcessingException;
import com.azubike.ellipsis.droneapplication.exception.NotFoundException;
import com.azubike.ellipsis.droneapplication.medications.domian.Image;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.repository.ImageRepository;
import com.azubike.ellipsis.droneapplication.medications.repository.MedicationRepository;
import com.azubike.ellipsis.droneapplication.medications.services.MedicationService;
import com.azubike.ellipsis.droneapplication.medications.web.dto.ImageDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsPageList;
import com.azubike.ellipsis.droneapplication.medications.web.mappers.ImageMapper;
import com.azubike.ellipsis.droneapplication.medications.web.mappers.MedicationsMapper;
import com.azubike.ellipsis.droneapplication.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicationsServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationsMapper medicationsMapper;

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;

    private final String FILE_PATH = "test_image.webp";


    @Override
    public MedicationsPageList listMedications(final PageRequest pageable) {
        final Page<Medication> medicationsPage = medicationRepository.findAll(pageable);

        final List<MedicationsDto> medicationsDtos = medicationsPage.getContent()
                .stream().map(medicationsMapper::medicationsToDto).toList();
        return new MedicationsPageList(medicationsDtos, PageRequest.of(
                medicationsPage.getPageable().getPageNumber(),
                medicationsPage.getPageable().getPageSize(), Sort.Direction.DESC, "id"), medicationsPage.getTotalPages());
    }


    @Transactional
    @Override
    public MedicationsDto createMedication(final MedicationsDto medicationsDto, MultipartFile medicationImage) {
        final Optional<Medication> optional = medicationRepository.findMedicationByCode(medicationsDto.getCode());
        if (optional.isPresent())
            throw new ConflictException(String.format("Medication with code %s already exists", medicationsDto.getCode()));
        try {
            ImageDto imageDto = isImageFalsy(medicationImage) ? createDefaultImage(FILE_PATH) : createImageFromMultipart(medicationImage);
            medicationsDto.setImageDto(imageDto);
            final Medication medication = medicationRepository.save(medicationsMapper.dtoToMedication(medicationsDto));
            medication.getImage().setMedication(medication);
            return medicationsMapper.medicationsToDto(medication);

        } catch (Exception ex) {
            throw new ImageProcessingException(ex.getMessage());
        }

    }

    private static boolean isImageFalsy(final MultipartFile medicationImage) {
        return medicationImage == null || Objects.requireNonNull(medicationImage.getOriginalFilename()).isEmpty();
    }


    private ImageDto createImageFromMultipart(MultipartFile medicationImage) throws IOException {
        return ImageDto.builder().content(medicationImage.getBytes())
                .size(medicationImage.getSize()).name(Utils.randomizeName(medicationImage)).build();
    }

    private ImageDto createDefaultImage(String filePath) throws IOException {
        ImageDto imageDto;
        var file = new ClassPathResource(filePath).getFile();
        final byte[] content = Files.readAllBytes(file.toPath());
        String fileName = String.join("-", Utils.generateRandomString(10), file.getName());
        imageDto = ImageDto.builder().size((long) content.length).content(content).name(fileName).build();
        return imageDto;
    }

    @Override
    public MedicationsDto findMedicationById(final Long id) {
        final Medication foundMedication = medicationRepository
                .findById(id).orElseThrow(() -> new NotFoundException(String.format("Medication with id %d not found", id)));
        return medicationsMapper.medicationsToDto(foundMedication);
    }

    @Override
    public void deleteMedication(final Long id) {
        var foundMedication = this.findMedicationById(id);
        medicationRepository.deleteById(foundMedication.getId());
    }

    @Override
    public MedicationsDto findMedicationByCode(final String code) {
        final Medication foundMedication = medicationRepository.findMedicationByCode(code).orElseThrow(() ->
                new NotFoundException(String.format("Medication with code %s not found", code)));
        return medicationsMapper.medicationsToDto(foundMedication);

    }

    @Override
    @Transactional
    public MedicationsDto updateMedication(final MedicationsDto medicationsDto, MultipartFile medicationImage, Long id) {

        final Medication medication = medicationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Medication with id %d not found", id)));
        //if there is an image create an image from the multipart file and set the medicationId to the foundMed
        try {
            if (!isImageFalsy(medicationImage)) {
                medicationsDto.setImageDto(createImageFromMultipart(medicationImage));
                medicationsDto.getImageDto().setId(medication.getImage().getId());
                final Image savedImage = imageRepository.save(imageMapper.dtoToImage(medicationsDto.getImageDto()));
                savedImage.setMedication(medication);
            }
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true)
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(medicationsDto, medication);

            return medicationsMapper.medicationsToDto(medication);

        } catch (Exception ex) {
            throw new ImageProcessingException(ex.getMessage());
        }
    }


    @Override
    public List<MedicationsDto> findMedicationByName(final String name) {
        return medicationRepository.findMedicationByName(name).
                stream().map(medicationsMapper::medicationsToDto).toList();
    }

}
