package com.azubike.ellipsis.droneapplication.medications.domian;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "medication_image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "medication")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 512, unique = true, nullable = false)
    private String name;

    private Long size;

    @Lob
    @Column(length = 5000000)
    private byte[] content;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp uploadTime;


    @OneToOne
    @JoinColumn(name = "medication_id")
    private Medication medication;
}
