package com.azubike.ellipsis.droneapplication.drones.web.mappers;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.web.dto.CreateDroneDto;
import com.azubike.ellipsis.droneapplication.drones.web.dto.DroneDto;
import com.azubike.ellipsis.droneapplication.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class DroneMapper {

    protected DroneState droneState;
    protected DroneModel droneModel;

    @Autowired
     protected Utils utils;

    @Mapping(target = "state", expression = "java(createDroneDto.getDroneState() != null ? droneState.valueOf(createDroneDto.getDroneState().toUpperCase()) : null)")
    @Mapping(target = "model", expression = "java(droneModel.valueOf(createDroneDto.getDroneModel().toUpperCase()))")
    @Mapping(target = "medications", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract DroneDto createDroneDtoToDroneDto(CreateDroneDto createDroneDto);


    @Mapping(target = "state" , expression = "java(droneDto.getState()== null ?  droneState.IDLE : droneDto.getState() )")
    @Mapping(target = "batteryCapacity" ,expression = "java(droneDto.getBatteryCapacity() == null ? 100 : droneDto.getBatteryCapacity())")
    @Mapping(target = "serialNumber" , expression = "java(droneDto.getSerialNumber() == null ? utils.generateRandomString(20) : droneDto.getSerialNumber())")
    public abstract Drone dtoToDrone(DroneDto droneDto);


    public abstract DroneDto droneToDto(Drone drone);


}
