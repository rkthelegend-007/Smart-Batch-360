package com.batching.backend.repository;

import com.batching.backend.entity.VehicleData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleData, Integer> {
    boolean existsByVehicleId(int vehicleId);
}
