package com.batching.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operatorName;
    private double cementWeight;
    private double sandWeight;
    private double aggregateWeight;

    private LocalDateTime timestamp = LocalDateTime.now();

    // 👉 Constructors
    public Report() {}

    public Report(String operatorName, double cementWeight, double sandWeight, double aggregateWeight) {
        this.operatorName = operatorName;
        this.cementWeight = cementWeight;
        this.sandWeight = sandWeight;
        this.aggregateWeight = aggregateWeight;
        this.timestamp = LocalDateTime.now();
    }

    // 👉 Getters and Setters

    public Long getId() {
        return id;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public double getCementWeight() {
        return cementWeight;
    }

    public void setCementWeight(double cementWeight) {
        this.cementWeight = cementWeight;
    }

    public double getSandWeight() {
        return sandWeight;
    }

    public void setSandWeight(double sandWeight) {
        this.sandWeight = sandWeight;
    }

    public double getAggregateWeight() {
        return aggregateWeight;
    }

    public void setAggregateWeight(double aggregateWeight) {
        this.aggregateWeight = aggregateWeight;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
