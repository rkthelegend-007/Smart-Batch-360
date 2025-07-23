package com.batching.model;

import javafx.beans.property.*;

public class DriverData {
    private final IntegerProperty driverId = new SimpleIntegerProperty();
    private final StringProperty driverName = new SimpleStringProperty();
    private final StringProperty contactNumber = new SimpleStringProperty();

    // Getters and setters with JavaFX bindings
    public int getDriverId() {
        return driverId.get();
    }

    public void setDriverId(int driverId) {
        this.driverId.set(driverId);
    }

    public IntegerProperty driverIdProperty() {
        return driverId;
    }

    public String getDriverName() {
        return driverName.get();
    }

    public void setDriverName(String driverName) {
        this.driverName.set(driverName);
    }

    public StringProperty driverNameProperty() {
        return driverName;
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber.set(contactNumber);
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }
}
