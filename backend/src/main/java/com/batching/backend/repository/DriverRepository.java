package com.batching.backend.repository;

import com.batching.backend.entity.DriverData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverData, Integer> {
    boolean existsByDriverId(int driverId);
}
