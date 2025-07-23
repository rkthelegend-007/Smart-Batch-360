package com.batching.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

import com.batching.backend.entity.CycleData;
public interface CycleDataRepository extends JpaRepository<CycleData, Integer> {

    @Query("SELECT DISTINCT c.batchNumber FROM CycleData c WHERE c.batchNumber IS NOT NULL")
    List<String> findDistinctBatchNumbers();

    List<CycleData> findByBatchNumber(@Param("batchNumber") String batchNumber);
}
