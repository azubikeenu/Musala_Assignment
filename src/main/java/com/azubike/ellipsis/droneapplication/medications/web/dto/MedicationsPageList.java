package com.azubike.ellipsis.droneapplication.medications.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class MedicationsPageList extends PageImpl<MedicationsDto> implements Serializable {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MedicationsPageList(
            @JsonProperty("content") List<MedicationsDto> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }


    public MedicationsPageList(final List<MedicationsDto> content, final Pageable pageable, final long total) {
        super(content, pageable, total);
    }

    public MedicationsPageList(final List<MedicationsDto> content) {
        super(content);
    }

}
