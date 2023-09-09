package com.azubike.ellipsis.droneapplication.medications.repository;

import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<Medication> findMedicationByCode(String code);
    List<Medication> findMedicationByName(String name);
}
