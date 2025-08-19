package com.batching.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;

@JsonIgnoreProperties(ignoreUnknown = true)  // 👈 This will ignore extra JSON fields
public class BatchData {

    private final IntegerProperty srNo = new SimpleIntegerProperty();
    private final DoubleProperty batchNumber = new SimpleDoubleProperty();
    private final DoubleProperty batchQuantity = new SimpleDoubleProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty siteName = new SimpleStringProperty();
    private final StringProperty driverName = new SimpleStringProperty();
    private final StringProperty vehicleNumber = new SimpleStringProperty();

    // --- Sr No ---
    public int getSrNo() { return srNo.get(); }
    public void setSrNo(int value) { srNo.set(value); }
    public IntegerProperty srNoProperty() { return srNo; }

    // --- Batch Number ---
    public double getBatchNumber() { return batchNumber.get(); }
    public void setBatchNumber(double value) { batchNumber.set(value); }
    public DoubleProperty batchNumberProperty() { return batchNumber; }

    // --- Batch Quantity ---
    public double getBatchQuantity() { return batchQuantity.get(); }
    public void setBatchQuantity(double value) { batchQuantity.set(value); }
    public DoubleProperty batchQuantityProperty() { return batchQuantity; }

    // --- Customer Name ---
    public String getCustomerName() { return customerName.get(); }
    public void setCustomerName(String value) { customerName.set(value); }
    public StringProperty customerNameProperty() { return customerName; }

    // --- Site Name ---
    public String getSiteName() { return siteName.get(); }
    public void setSiteName(String value) { siteName.set(value); }
    public StringProperty siteNameProperty() { return siteName; }

    // --- Driver Name ---
    public String getDriverName() { return driverName.get(); }
    public void setDriverName(String value) { driverName.set(value); }
    public StringProperty driverNameProperty() { return driverName; }

    // --- Vehicle Number ---
    public String getVehicleNumber() { return vehicleNumber.get(); }
    public void setVehicleNumber(String value) { vehicleNumber.set(value); }
    public StringProperty vehicleNumberProperty() { return vehicleNumber; }
}
