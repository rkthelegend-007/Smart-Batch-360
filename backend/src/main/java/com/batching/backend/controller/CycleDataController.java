package com.batching.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batching.backend.entity.CycleData;
import com.batching.backend.repository.CycleDataRepository;

@RestController
@RequestMapping("/api/cycle")
@CrossOrigin
public class CycleDataController {

    @Autowired
    private CycleDataRepository repository;

    @GetMapping("/all")
    public List<CycleData> getAll() {
        return repository.findAll();
    }

    @GetMapping("/batch-numbers")
    public List<String> getAllBatchNumbers() {
    return repository.findDistinctBatchNumbers();
}

@GetMapping("/by-batch/{batchNumber}")
public List<CycleData> getByBatch(@PathVariable String batchNumber) {
    return repository.findByBatchNumber(batchNumber);
}
}
