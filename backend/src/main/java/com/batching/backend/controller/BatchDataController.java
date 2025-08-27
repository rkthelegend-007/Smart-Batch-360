package com.batching.backend.controller;

import com.batching.backend.entity.BatchData;
import com.batching.backend.repository.BatchDataRepository;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/batch-data")
public class BatchDataController {

    private final BatchDataRepository batchDataRepository;

    public BatchDataController(BatchDataRepository batchDataRepository) {
        this.batchDataRepository = batchDataRepository;
    }

    // ✅ Fetch all batches
    @GetMapping
    public List<BatchData> getAllBatchData() {
        return batchDataRepository.findAll();
    }

    // ✅ Get distinct batch numbers (for dropdown)
    @GetMapping("/numbers")
    public List<Double> getAllBatchNumbers() {
        return batchDataRepository.findDistinctBatchNumbers();
    }

    // ✅ Fetch by batch number
    @GetMapping("/batch/{batchNumber}")
    public List<BatchData> getByBatchNumber(@PathVariable Double batchNumber) {
        return batchDataRepository.findByBatchNumber(batchNumber);
    }

    // ✅ Fetch by a single cycle date
    @GetMapping("/date/{cycleDate}")
    public List<BatchData> getByCycleDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cycleDate) {
        return batchDataRepository.findByCycleDate(cycleDate);
    }

    // ✅ Fetch by date range
    @GetMapping("/date-range")
    public List<BatchData> getByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return batchDataRepository.findByCycleDateBetween(startDate, endDate);
    }

    // ✅ Flexible filter (batchNumber + date range optional)
    @GetMapping("/filter")
    public List<BatchData> filterBatchData(
            @RequestParam(required = false) Double batchNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Case 1: batch number + date range
        if (batchNumber != null && startDate != null && endDate != null) {
            return batchDataRepository.findByBatchNumberAndCycleDateBetween(batchNumber, startDate, endDate);
        }

        // Case 2: batch number only
        if (batchNumber != null) {
            return batchDataRepository.findByBatchNumber(batchNumber);
        }

        // Case 3: date range only
        if (startDate != null && endDate != null) {
            return batchDataRepository.findByCycleDateBetween(startDate, endDate);
        }

        // Default → return all data
        return batchDataRepository.findAll();
    }
}
