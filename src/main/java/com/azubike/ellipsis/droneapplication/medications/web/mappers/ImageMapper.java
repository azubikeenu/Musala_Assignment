package com.azubike.ellipsis.droneapplication.medications.web.mappers;

import com.azubike.ellipsis.droneapplication.medications.domian.Image;
import com.azubike.ellipsis.droneapplication.medications.web.dto.ImageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ImageMapper {
    @Mapping(target = "uploadTime", ignore = true)
    @Mapping(target = "medication", ignore = true)
    Image dtoToImage(ImageDto imageDto);

    ImageDto imageToDto(Image image);
}
