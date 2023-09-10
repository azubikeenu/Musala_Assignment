package com.azubike.ellipsis.droneapplication.drones.domain;

import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    private BigDecimal weightLimit;

    private Integer batteryCapacity;

    private DroneState state;

    @ManyToMany
    @JoinTable(
            name = "drone_medication",
            joinColumns = @JoinColumn(name = "drone_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private List<Medication> medications = new ArrayList<>();


}
