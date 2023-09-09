package com.azubike.ellipsis.droneapplication.medications.domian;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "image")
@Table(name = "medications")

public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal weight ;


    @Column(unique = true)
    private String code;


    @OneToOne(mappedBy = "medication" , cascade = CascadeType.ALL )
    private Image image;


    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;


    @ManyToMany(mappedBy = "medications")
    private Set<Drone> drones = new HashSet<>();

}
