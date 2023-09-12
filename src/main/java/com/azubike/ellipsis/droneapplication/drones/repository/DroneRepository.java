package com.azubike.ellipsis.droneapplication.drones.repository;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);

    List<Drone> findAllByStateIn(List<DroneState> droneStates);

    @Query(value = "SELECT * FROM drone WHERE battery_capacity < 25", nativeQuery = true)
    List<Drone> findLowBatteryDrones();
}
