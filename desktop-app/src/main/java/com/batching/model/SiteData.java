package com.batching.model;

import javafx.beans.property.*;

public class SiteData {

    private final IntegerProperty siteId = new SimpleIntegerProperty();
    private final StringProperty siteName = new SimpleStringProperty();
    private final StringProperty siteAddress = new SimpleStringProperty();
    private final StringProperty contactInfo = new SimpleStringProperty();
    private final StringProperty gstNumber = new SimpleStringProperty();

    public int getSiteId() { return siteId.get(); }
    public void setSiteId(int value) { siteId.set(value); }
    public IntegerProperty siteIdProperty() { return siteId; }

    public String getSiteName() { return siteName.get(); }
    public void setSiteName(String value) { siteName.set(value); }
    public StringProperty siteNameProperty() { return siteName; }

    public String getSiteAddress() { return siteAddress.get(); }
    public void setSiteAddress(String value) { siteAddress.set(value); }
    public StringProperty siteAddressProperty() { return siteAddress; }

    public String getContactInfo() { return contactInfo.get(); }
    public void setContactInfo(String value) { contactInfo.set(value); }
    public StringProperty contactInfoProperty() { return contactInfo; }

    public String getGstNumber() { return gstNumber.get(); }
    public void setGstNumber(String value) { gstNumber.set(value); }
    public StringProperty gstNumberProperty() { return gstNumber; }
}