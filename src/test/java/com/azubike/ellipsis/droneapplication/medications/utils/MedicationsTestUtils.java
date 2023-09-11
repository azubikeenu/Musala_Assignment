package com.azubike.ellipsis.droneapplication.medications.utils;

import com.azubike.ellipsis.droneapplication.medications.domian.Image;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.web.dto.ImageDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

public interface MedicationsTestUtils {
     static Image createImage(String filePath) throws IOException {
        var file = new ClassPathResource(filePath).getFile();
        final byte[] content = Files.readAllBytes(file.toPath());
        return Image.builder().size((long) content.length)
                .name(file.getName()).content(content).build();
    }

     static ImageDto createImageDto(String filePath) throws IOException {
        var file = new ClassPathResource(filePath).getFile();
        final byte[] content = Files.readAllBytes(file.toPath());
        return ImageDto.builder().size((long) content.length)
                .name(file.getName()).content(content).build();
    }


     static Medication createValidMedication(String filePath) throws IOException {
        return Medication.builder().image(MedicationsTestUtils.createImage(filePath)).
                name("test_name").weight(new BigDecimal("2.5")).code("another_code").build();
    }


     static MedicationsDto createValidMedicationsDto(String filePath) throws Exception {
        return MedicationsDto.builder().imageDto(MedicationsTestUtils.createImageDto(filePath)).
                name("test_name").weight(new BigDecimal("2.5")).code("another_code").build();
    }

     static MedicationsDto updateValidMedicationsDto(String filePath) throws Exception {
        return MedicationsDto.builder().imageDto(MedicationsTestUtils.createImageDto(filePath)).
                name("updated_name").weight(new BigDecimal("2.5")).code("updated_code").build();
    }
}
