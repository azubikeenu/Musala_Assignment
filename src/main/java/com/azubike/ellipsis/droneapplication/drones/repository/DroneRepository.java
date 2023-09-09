package com.azubike.ellipsis.droneapplication.drones.repository;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone,Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);
}
