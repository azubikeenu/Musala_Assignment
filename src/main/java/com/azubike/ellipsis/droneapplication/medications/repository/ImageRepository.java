package com.azubike.ellipsis.droneapplication.medications.repository;

import com.azubike.ellipsis.droneapplication.medications.domian.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ImageRepository extends JpaRepository<Image ,Integer> {
    @Transactional
    @Modifying
    @Query(value = "DELETE from medication_image  WHERE id = :id ", nativeQuery = true)
    void removeImageById(@Param("id") Integer id);
}
