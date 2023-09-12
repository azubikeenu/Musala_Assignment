package com.azubike.ellipsis.droneapplication.drones.repository;

import com.azubike.ellipsis.droneapplication.drones.domain.AuditDrone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditDrone ,Integer> {
}
