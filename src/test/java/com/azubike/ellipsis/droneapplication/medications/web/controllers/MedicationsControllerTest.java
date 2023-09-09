package com.azubike.ellipsis.droneapplication.medications.web.controllers;

import com.azubike.ellipsis.droneapplication.medications.services.MedicationService;
import com.azubike.ellipsis.droneapplication.medications.utils.TestUtils;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsDto;
import com.azubike.ellipsis.droneapplication.medications.web.dto.MedicationsPageList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationsController.class)
class MedicationsControllerTest {
    private final String FILE_PATH = "test_image.webp";
    private final String BASE_URL = "/api/v1/medications";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MedicationService medicationService;


    @Test
    public void testGetMedications() throws Exception {
        // Mock the behavior of medicationService.listMedications
        final List<MedicationsDto> medications = List.of(TestUtils.createValidMedicationsDto(FILE_PATH),
                TestUtils.createValidMedicationsDto(FILE_PATH)
        );
        final MedicationsPageList medicationsPageList = new MedicationsPageList(medications);

        when(medicationService.listMedications(any(PageRequest.class)))
                .thenReturn(medicationsPageList);

        // Perform a GET request and verify the response
        mockMvc.perform(get(BASE_URL)
                        .param("pageNumber", "0")
                        .param("pageSize", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));

        // Verify that medicationService.listMedications was called with the expected arguments
        verify(medicationService, times(1)).listMedications(PageRequest.of(0, 25));
    }


    @Test
    void create() throws Exception {
        final MedicationsDto medicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);
        MockMultipartFile file =
                new MockMultipartFile("medicationsImage", "test-image.jpg",
                        "image/jpeg", "image data".getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", medicationsDto.getName());
        formData.add("weight", String.valueOf(medicationsDto.getWeight()));

        when(medicationService.createMedication(any(MedicationsDto.class),
                any(MultipartFile.class))).thenReturn(medicationsDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file(file)
                        .params(formData)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(medicationsDto.getName()))
                .andExpect(jsonPath("$.weight").value(medicationsDto.getWeight()))
                .andExpect(jsonPath("$.code").value(medicationsDto.getCode()));


    }

    @Test
    void itsShouldPerformValidationOnFormData() throws Exception{

        final MedicationsDto medicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);
        MockMultipartFile file =
                new MockMultipartFile("invalid type", "text.txt",
                        "text/txt", "invalid type".getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", medicationsDto.getName());

        when(medicationService.createMedication(any(MedicationsDto.class),
                any(MultipartFile.class))).thenReturn(medicationsDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file(file)
                        .params(formData)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[medication weight is a required field]"));

    }

    @Test
    void findById() throws Exception {
        final MedicationsDto medicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);
        when(medicationService.findMedicationById(anyLong())).thenReturn(medicationsDto);
        mockMvc.perform(get(BASE_URL + "/{id}", anyLong()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(medicationsDto.getName()))
                .andExpect(jsonPath("$.weight").value(medicationsDto.getWeight()));

        ;
    }

    @Test
    void update() throws Exception {
        final MedicationsDto validMedicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);
        final MedicationsDto updatedMedicationsDto = TestUtils.createValidMedicationsDto(FILE_PATH);

        var name = "Updated_Name";
        var weight = "123";
        updatedMedicationsDto.setName(name);
        updatedMedicationsDto.setWeight(new BigDecimal(weight));
        var id = 1L;
        validMedicationsDto.setId(1L);
        MockMultipartFile file =
                new MockMultipartFile("medicationsImage", "test-image.jpg",
                        "image/jpeg", "image data".getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", name);
        formData.add("weight", weight);

        when(medicationService.updateMedication(any(MedicationsDto.class),
                any(MultipartFile.class), anyLong())).thenReturn(updatedMedicationsDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart(String.format("%s/%d", BASE_URL, id))
                        .file(file)
                        .params(formData)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedMedicationsDto.getName()))
                .andExpect(jsonPath("$.weight").value(updatedMedicationsDto.getWeight()));

    }

    @Test
    public void testDeleteMedication() throws Exception {
        Long id = 1L;
        doNothing().when(medicationService).deleteMedication(id);
        mockMvc.perform(delete(String.format("%s/%d",BASE_URL ,id)))
                .andExpect(status().isOk())
                .andExpect(content().string("MEDICATION SUCCESSFULLY DELETED"));
        verify(medicationService, times(1)).deleteMedication(eq(id));
    }

}