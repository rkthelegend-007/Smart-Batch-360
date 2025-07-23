
package com.batching.model;

import java.time.LocalDateTime;

public class Report {
    private Long id;
    private String operatorName;
    private double cementWeight;
    private double sandWeight;
    private double aggregateWeight;
    private LocalDateTime timestamp;

    // Getters and Setters
    public Long getId() { return id; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public double getCementWeight() { return cementWeight; }
    public void setCementWeight(double cementWeight) { this.cementWeight = cementWeight; }
    public double getSandWeight() { return sandWeight; }
    public void setSandWeight(double sandWeight) { this.sandWeight = sandWeight; }
    public double getAggregateWeight() { return aggregateWeight; }
    public void setAggregateWeight(double aggregateWeight) { this.aggregateWeight = aggregateWeight; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
