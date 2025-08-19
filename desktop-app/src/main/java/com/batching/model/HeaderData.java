package com.batching.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeaderData {

    private final SimpleIntegerProperty headerId = new SimpleIntegerProperty();
    private final SimpleStringProperty companyName = new SimpleStringProperty();
    private final SimpleStringProperty companyAddress = new SimpleStringProperty();
    private final SimpleStringProperty contactNumber = new SimpleStringProperty();
    private final SimpleStringProperty gstNumber = new SimpleStringProperty();
    private final SimpleStringProperty pinCode = new SimpleStringProperty();
    private final SimpleStringProperty logoPath = new SimpleStringProperty();

    // === Jackson needs this to map 'id' field from backend ===
    @JsonProperty("id")
    public int getHeaderId() {
        return headerId.get();
    }

    @JsonProperty("id")
    public void setHeaderId(int id) {
        this.headerId.set(id);
    }

    public SimpleIntegerProperty headerIdProperty() {
        return headerId;
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String name) {
        this.companyName.set(name);
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress.get();
    }

    public void setCompanyAddress(String address) {
        this.companyAddress.set(address);
    }

    public SimpleStringProperty companyAddressProperty() {
        return companyAddress;
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public void setContactNumber(String contact) {
        this.contactNumber.set(contact);
    }

    public SimpleStringProperty contactNumberProperty() {
        return contactNumber;
    }

    public String getGstNumber() {
        return gstNumber.get();
    }

    public void setGstNumber(String gst) {
        this.gstNumber.set(gst);
    }

    public SimpleStringProperty gstNumberProperty() {
        return gstNumber;
    }

    public String getPinCode() {
        return pinCode.get();
    }

    public void setPinCode(String pin) {
        this.pinCode.set(pin);
    }

    public SimpleStringProperty pinCodeProperty() {
        return pinCode;
    }

    public String getLogoPath() {
        return logoPath.get();
    }

    public void setLogoPath(String path) {
        this.logoPath.set(path);
    }

    public SimpleStringProperty logoPathProperty() {
        return logoPath;
    }
}
