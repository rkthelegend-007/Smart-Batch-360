package com.batching.backend.service;

import com.batching.backend.entity.DriverData;
import com.batching.backend.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<DriverData> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<DriverData> getDriverById(int id) {
        return driverRepository.findById(id);
    }

    public DriverData saveDriver(DriverData driver) {
        return driverRepository.save(driver);
    }

    public void deleteDriver(int id) {
        driverRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return driverRepository.existsByDriverId(id);
    }
}
