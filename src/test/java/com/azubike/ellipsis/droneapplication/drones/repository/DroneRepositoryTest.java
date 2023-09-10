package com.azubike.ellipsis.droneapplication.drones.repository;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneModel;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DroneRepositoryTest {
    @Autowired
    DroneRepository droneRepository;

    @Test
    void findBySerialNumber() {
    }

    @Test
    void findIdleDrones() {
        final List<Drone> drones = List.of(
                Drone.builder().id(1L).model(DroneModel.CRUISERWEGHT).
                        batteryCapacity(20).serialNumber("ABCDE").state(DroneState.IDLE).build(),
                Drone.builder().id(2L).model(DroneModel.LIGHTWEIGHT).
                        batteryCapacity(100).serialNumber("EFGH").state(DroneState.LOADED).build(),
                Drone.builder().id(3L).model(DroneModel.MIDDLEWEIGHT).
                        batteryCapacity(80).serialNumber("IJKL").state(DroneState.LOADING).build() ,
                Drone.builder().id(4L).model(DroneModel.MIDDLEWEIGHT).
                        batteryCapacity(56).serialNumber("OPQR").state(DroneState.IDLE).build()

        );
        droneRepository.saveAll(drones);

        final List<Drone> idleDrones = droneRepository.findAllByState(DroneState.IDLE);

        assertThat(idleDrones.size()).isEqualTo(2);

    }
}