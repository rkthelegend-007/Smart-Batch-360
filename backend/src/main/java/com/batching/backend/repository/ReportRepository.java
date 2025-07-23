package com.batching.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batching.backend.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // You get all CRUD operations automatically!
}
