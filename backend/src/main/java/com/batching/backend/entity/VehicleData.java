package com.batching.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle_data")
public class VehicleData {

    @Id
    private int vehicleId;

    private String vehicleNumber;

    public VehicleData() {}

    public VehicleData(int vehicleId, String vehicleNumber) {
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
