package com.batching.backend.controller;

import com.batching.backend.entity.DriverData;
import com.batching.backend.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = "*")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<DriverData> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @PostMapping
    public ResponseEntity<String> saveDriver(@RequestBody DriverData driver) {
        driverService.saveDriver(driver);
        return ResponseEntity.ok("Driver saved successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDriver(@PathVariable int id, @RequestBody DriverData driver) {
        driver.setDriverId(id);
        driverService.saveDriver(driver);
        return ResponseEntity.ok("Driver updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable int id) {
        driverService.deleteDriver(id);
        return ResponseEntity.ok("Driver deleted successfully.");
    }

    @GetMapping("/check")
    public boolean checkDuplicate(@RequestParam int id) {
        return driverService.existsById(id);
    }
}
