package com.batching.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.batching.backend.entity.BatchData;

public interface BatchDataRepository extends JpaRepository<BatchData, Integer> {

    // Find by batch number
    List<BatchData> findByBatchNumber(Double batchNumber);

    // Find by cycle date
    List<BatchData> findByCycleDate(LocalDate cycleDate);

    // Find by date range
    List<BatchData> findByCycleDateBetween(LocalDate startDate, LocalDate endDate);

    // Find by batch number and date range
    List<BatchData> findByBatchNumberAndCycleDateBetween(Double batchNumber, LocalDate startDate, LocalDate endDate);

    // Get distinct batch numbers (for dropdown)
    @Query("SELECT DISTINCT b.batchNumber FROM BatchData b ORDER BY b.batchNumber ASC")
    List<Double> findDistinctBatchNumbers();
}
