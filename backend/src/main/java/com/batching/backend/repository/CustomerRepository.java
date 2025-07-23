package com.batching.backend.repository;

import com.batching.backend.entity.CustomerData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerData, Integer> {
    boolean existsByCustomerId(int customerId);
}
