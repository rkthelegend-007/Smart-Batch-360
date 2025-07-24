package com.batching.backend.repository;

import com.batching.backend.entity.HeaderData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeaderDataRepository extends JpaRepository<HeaderData, Integer> {
    boolean existsByCompanyName(String companyName);
}