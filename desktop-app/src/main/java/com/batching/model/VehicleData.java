package com.batching.model;

import javafx.beans.property.*;

public class VehicleData {

    private IntegerProperty vehicleId = new SimpleIntegerProperty();
    private StringProperty vehicleNumber = new SimpleStringProperty();

    public VehicleData() {}

    public VehicleData(int id, String number) {
        this.vehicleId.set(id);
        this.vehicleNumber.set(number);
    }

    public int getVehicleId() {
        return vehicleId.get();
    }

    public void setVehicleId(int id) {
        this.vehicleId.set(id);
    }

    public IntegerProperty vehicleIdProperty() {
        return vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber.get();
    }

    public void setVehicleNumber(String number) {
        this.vehicleNumber.set(number);
    }

    public StringProperty vehicleNumberProperty() {
        return vehicleNumber;
    }
}
