package com.batching.backend.controller;

import com.batching.backend.entity.VehicleData;
import com.batching.backend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository repository;

    @GetMapping
    public List<VehicleData> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public VehicleData create(@RequestBody VehicleData vehicle) {
        return repository.save(vehicle);
    }

    @PutMapping("/{id}")
    public VehicleData update(@PathVariable int id, @RequestBody VehicleData updated) {
        updated.setVehicleId(id);
        return repository.save(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @GetMapping("/check")
    public boolean checkDuplicate(@RequestParam int id) {
        return repository.existsByVehicleId(id);
    }
}
