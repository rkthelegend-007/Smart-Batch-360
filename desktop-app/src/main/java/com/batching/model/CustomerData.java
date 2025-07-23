package com.batching.model;

import javafx.beans.property.*;

public class CustomerData {

    private final IntegerProperty customerId = new SimpleIntegerProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty companyName = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty contactInfo = new SimpleStringProperty();
    private final StringProperty gstNumber = new SimpleStringProperty();

    public CustomerData() {}

    // Getters and setters with properties
    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int value) {
        customerId.set(value);
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String value) {
        customerName.set(value);
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String value) {
        companyName.set(value);
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String value) {
        address.set(value);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getContactInfo() {
        return contactInfo.get();
    }

    public void setContactInfo(String value) {
        contactInfo.set(value);
    }

    public StringProperty contactInfoProperty() {
        return contactInfo;
    }

    public String getGstNumber() {
        return gstNumber.get();
    }

    public void setGstNumber(String value) {
        gstNumber.set(value);
    }

    public StringProperty gstNumberProperty() {
        return gstNumber;
    }
}
