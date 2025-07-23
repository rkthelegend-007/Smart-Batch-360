package com.batching.backend.repository;

import com.batching.backend.entity.SiteData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<SiteData, Integer> {
    boolean existsBySiteId(int siteId);
}
