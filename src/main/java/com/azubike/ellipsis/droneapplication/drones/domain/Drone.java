package com.azubike.ellipsis.droneapplication.drones.domain;

import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    private BigDecimal weightLimit;

    private String batteryCapacity;

    private DroneState state;

    @ManyToMany
    @JoinTable(
            name = "drone_medication",
            joinColumns = @JoinColumn(name = "drone_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private List<Medication> medications = new ArrayList<>();


}
