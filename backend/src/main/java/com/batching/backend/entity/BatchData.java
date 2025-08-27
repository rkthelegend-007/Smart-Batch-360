package com.batching.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "BATCH_DATA")
public class BatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SRNO")
    private Integer srNo;

    @Column(name = "BATCHQUANTITY")
    private Double batchQuantity;

    @Column(name = "CUSTOMERNAME")
    private String customerName;

    @Column(name = "SITENAME")
    private String siteName;

    @Column(name = "DRIVERNAME")
    private String driverName;

    @Column(name = "VEHICLENUMBER")
    private String vehicleNumber;

    @Column(name = "PERCYCLEQUANTITY")
    private Double perCycleQuantity;

    @Column(name = "BATCHNUMBER")
    private Double batchNumber;

    @Column(name = "CYCLEDATE")
    private LocalDate cycleDate;

    @Column(name = "CYCLETIME")
    private LocalTime cycleTime;

    @Column(name = "CYCLENUMBER")
    private Integer cycleNumber;

    @Column(name = "SHIFTNUMBER")
    private Integer shiftNumber;

    @Column(name = "RECIPEID")
    private Integer recipeId;

    @Column(name = "RECIPENAME")
    private String recipeName;

    // --- Materials (1..20)
    @Column(name = "MAT1_NAME") private String mat1Name;
    @Column(name = "MAT1_TARGET") private String mat1Target;
    @Column(name = "MAT1_SETPOINT") private String mat1Setpoint;
    @Column(name = "MAT1_ACHIEVED") private String mat1Achieved;

    @Column(name = "MAT2_NAME") private String mat2Name;
    @Column(name = "MAT2_TARGET") private String mat2Target;
    @Column(name = "MAT2_SETPOINT") private String mat2Setpoint;
    @Column(name = "MAT2_ACHIEVED") private String mat2Achieved;

    @Column(name = "MAT3_NAME") private String mat3Name;
    @Column(name = "MAT3_TARGET") private String mat3Target;
    @Column(name = "MAT3_SETPOINT") private String mat3Setpoint;
    @Column(name = "MAT3_ACHIEVED") private String mat3Achieved;

    @Column(name = "MAT4_NAME") private String mat4Name;
    @Column(name = "MAT4_TARGET") private String mat4Target;
    @Column(name = "MAT4_SETPOINT") private String mat4Setpoint;
    @Column(name = "MAT4_ACHIEVED") private String mat4Achieved;

    @Column(name = "MAT5_NAME") private String mat5Name;
    @Column(name = "MAT5_TARGET") private String mat5Target;
    @Column(name = "MAT5_SETPOINT") private String mat5Setpoint;
    @Column(name = "MAT5_ACHIEVED") private String mat5Achieved;

    @Column(name = "MAT6_NAME") private String mat6Name;
    @Column(name = "MAT6_TARGET") private String mat6Target;
    @Column(name = "MAT6_SETPOINT") private String mat6Setpoint;
    @Column(name = "MAT6_ACHIEVED") private String mat6Achieved;

    @Column(name = "MAT7_NAME") private String mat7Name;
    @Column(name = "MAT7_TARGET") private String mat7Target;
    @Column(name = "MAT7_SETPOINT") private String mat7Setpoint;
    @Column(name = "MAT7_ACHIEVED") private String mat7Achieved;

    @Column(name = "MAT8_NAME") private String mat8Name;
    @Column(name = "MAT8_TARGET") private String mat8Target;
    @Column(name = "MAT8_SETPOINT") private String mat8Setpoint;
    @Column(name = "MAT8_ACHIEVED") private String mat8Achieved;

    @Column(name = "MAT9_NAME") private String mat9Name;
    @Column(name = "MAT9_TARGET") private String mat9Target;
    @Column(name = "MAT9_SETPOINT") private String mat9Setpoint;
    @Column(name = "MAT9_ACHIEVED") private String mat9Achieved;

    @Column(name = "MAT10_NAME") private String mat10Name;
    @Column(name = "MAT10_TARGET") private String mat10Target;
    @Column(name = "MAT10_SETPOINT") private String mat10Setpoint;
    @Column(name = "MAT10_ACHIEVED") private String mat10Achieved;

    @Column(name = "MAT11_NAME") private String mat11Name;
    @Column(name = "MAT11_TARGET") private String mat11Target;
    @Column(name = "MAT11_SETPOINT") private String mat11Setpoint;
    @Column(name = "MAT11_ACHIEVED") private String mat11Achieved;

    @Column(name = "MAT12_NAME") private String mat12Name;
    @Column(name = "MAT12_TARGET") private String mat12Target;
    @Column(name = "MAT12_SETPOINT") private String mat12Setpoint;
    @Column(name = "MAT12_ACHIEVED") private String mat12Achieved;

    @Column(name = "MAT13_NAME") private String mat13Name;
    @Column(name = "MAT13_TARGET") private String mat13Target;
    @Column(name = "MAT13_SETPOINT") private String mat13Setpoint;
    @Column(name = "MAT13_ACHIEVED") private String mat13Achieved;

    @Column(name = "MAT14_NAME") private String mat14Name;
    @Column(name = "MAT14_TARGET") private String mat14Target;
    @Column(name = "MAT14_SETPOINT") private String mat14Setpoint;
    @Column(name = "MAT14_ACHIEVED") private String mat14Achieved;

    @Column(name = "MAT15_NAME") private String mat15Name;
    @Column(name = "MAT15_TARGET") private String mat15Target;
    @Column(name = "MAT15_SETPOINT") private String mat15Setpoint;
    @Column(name = "MAT15_ACHIEVED") private String mat15Achieved;

    @Column(name = "MAT16_NAME") private String mat16Name;
    @Column(name = "MAT16_TARGET") private String mat16Target;
    @Column(name = "MAT16_SETPOINT") private String mat16Setpoint;
    @Column(name = "MAT16_ACHIEVED") private String mat16Achieved;

    @Column(name = "MAT17_NAME") private String mat17Name;
    @Column(name = "MAT17_TARGET") private String mat17Target;
    @Column(name = "MAT17_SETPOINT") private String mat17Setpoint;
    @Column(name = "MAT17_ACHIEVED") private String mat17Achieved;

    @Column(name = "MAT18_NAME") private String mat18Name;
    @Column(name = "MAT18_TARGET") private String mat18Target;
    @Column(name = "MAT18_SETPOINT") private String mat18Setpoint;
    @Column(name = "MAT18_ACHIEVED") private String mat18Achieved;

    @Column(name = "MAT19_NAME") private String mat19Name;
    @Column(name = "MAT19_TARGET") private String mat19Target;
    @Column(name = "MAT19_SETPOINT") private String mat19Setpoint;
    @Column(name = "MAT19_ACHIEVED") private String mat19Achieved;

    @Column(name = "MAT20_NAME") private String mat20Name;
    @Column(name = "MAT20_TARGET") private String mat20Target;
    @Column(name = "MAT20_SETPOINT") private String mat20Setpoint;
    @Column(name = "MAT20_ACHIEVED") private String mat20Achieved;

    // --- Totals
    @Column(name = "TARGET_TOTAL")
    private String targetTotal;

    @Column(name = "SETPOINT_TOTAL")
    private String setpointTotal;

    @Column(name = "ACHIEVED_TOTAL")
    private String achievedTotal;

    @Column(name = "ORDERID")
    private Integer orderId;

    @Column(name = "ORDERQUANTITY")
    private Double orderQuantity;

    @Column(name = "CUSTOMERADD")
    private String customerAdd;

    @Column(name = "SITEADD")
    private String siteAdd;

    @Column(name = "CREATEDATE")
    private java.sql.Timestamp createDate;

    public Integer getSrNo() {
    return srNo;
}

public void setSrNo(Integer srNo) {
    this.srNo = srNo;
}

public Double getBatchQuantity() {
    return batchQuantity;
}

public void setBatchQuantity(Double batchQuantity) {
    this.batchQuantity = batchQuantity;
}

public String getCustomerName() {
    return customerName;
}

public void setCustomerName(String customerName) {
    this.customerName = customerName;
}

public String getSiteName() {
    return siteName;
}

public void setSiteName(String siteName) {
    this.siteName = siteName;
}

public String getDriverName() {
    return driverName;
}

public void setDriverName(String driverName) {
    this.driverName = driverName;
}

public String getVehicleNumber() {
    return vehicleNumber;
}

public void setVehicleNumber(String vehicleNumber) {
    this.vehicleNumber = vehicleNumber;
}

public Double getPerCycleQuantity() {
    return perCycleQuantity;
}

public void setPerCycleQuantity(Double perCycleQuantity) {
    this.perCycleQuantity = perCycleQuantity;
}

public Double getBatchNumber() {
    return batchNumber;
}

public void setBatchNumber(Double batchNumber) {
    this.batchNumber = batchNumber;
}

public LocalDate getCycleDate() {
    return cycleDate;
}

public void setCycleDate(LocalDate cycleDate) {
    this.cycleDate = cycleDate;
}

public LocalTime getCycleTime() {
    return cycleTime;
}

public void setCycleTime(LocalTime cycleTime) {
    this.cycleTime = cycleTime;
}

public Integer getCycleNumber() {
    return cycleNumber;
}

public void setCycleNumber(Integer cycleNumber) {
    this.cycleNumber = cycleNumber;
}

public Integer getShiftNumber() {
    return shiftNumber;
}

public void setShiftNumber(Integer shiftNumber) {
    this.shiftNumber = shiftNumber;
}

public Integer getRecipeId() {
    return recipeId;
}

public void setRecipeId(Integer recipeId) {
    this.recipeId = recipeId;
}

public String getRecipeName() {
    return recipeName;
}

public void setRecipeName(String recipeName) {
    this.recipeName = recipeName;
}

public Integer getOrderId() {
    return orderId;
}

public void setOrderId(Integer orderId) {
    this.orderId = orderId;
}

public Double getOrderQuantity() {
    return orderQuantity;
}

public void setOrderQuantity(Double orderQuantity) {
    this.orderQuantity = orderQuantity;
}

    public String getMat1Name() { return mat1Name; }
    public void setMat1Name(String mat1Name) { this.mat1Name = mat1Name; }

    public String getMat1Target() { return mat1Target; }
    public void setMat1Target(String mat1Target) { this.mat1Target = mat1Target; }

    public String getMat1Setpoint() { return mat1Setpoint; }
    public void setMat1Setpoint(String mat1Setpoint) { this.mat1Setpoint = mat1Setpoint; }

    public String getMat1Achieved() { return mat1Achieved; }
    public void setMat1Achieved(String mat1Achieved) { this.mat1Achieved = mat1Achieved; }

    public String getMat2Name() { return mat2Name; }
    public void setMat2Name(String mat2Name) { this.mat2Name = mat2Name; }

    public String getMat2Target() { return mat2Target; }
    public void setMat2Target(String mat2Target) { this.mat2Target = mat2Target; }

    public String getMat2Setpoint() { return mat2Setpoint; }
    public void setMat2Setpoint(String mat2Setpoint) { this.mat2Setpoint = mat2Setpoint; }

    public String getMat2Achieved() { return mat2Achieved; }
    public void setMat2Achieved(String mat2Achieved) { this.mat2Achieved = mat2Achieved; }

    public String getMat3Name() { return mat3Name; }
    public void setMat3Name(String mat3Name) { this.mat3Name = mat3Name; }

    public String getMat3Target() { return mat3Target; }
    public void setMat3Target(String mat3Target) { this.mat3Target = mat3Target; }

    public String getMat3Setpoint() { return mat3Setpoint; }
    public void setMat3Setpoint(String mat3Setpoint) { this.mat3Setpoint = mat3Setpoint; }

    public String getMat3Achieved() { return mat3Achieved; }
    public void setMat3Achieved(String mat3Achieved) { this.mat3Achieved = mat3Achieved; }

    public String getMat4Name() { return mat4Name; }
    public void setMat4Name(String mat4Name) { this.mat4Name = mat4Name; }

    public String getMat4Target() { return mat4Target; }
    public void setMat4Target(String mat4Target) { this.mat4Target = mat4Target; }

    public String getMat4Setpoint() { return mat4Setpoint; }
    public void setMat4Setpoint(String mat4Setpoint) { this.mat4Setpoint = mat4Setpoint; }

    public String getMat4Achieved() { return mat4Achieved; }
    public void setMat4Achieved(String mat4Achieved) { this.mat4Achieved = mat4Achieved; }

    public String getMat5Name() { return mat5Name; }
    public void setMat5Name(String mat5Name) { this.mat5Name = mat5Name; }

    public String getMat5Target() { return mat5Target; }
    public void setMat5Target(String mat5Target) { this.mat5Target = mat5Target; }

    public String getMat5Setpoint() { return mat5Setpoint; }
    public void setMat5Setpoint(String mat5Setpoint) { this.mat5Setpoint = mat5Setpoint; }

    public String getMat5Achieved() { return mat5Achieved; }
    public void setMat5Achieved(String mat5Achieved) { this.mat5Achieved = mat5Achieved; }

    public String getMat6Name() { return mat6Name; }
    public void setMat6Name(String mat6Name) { this.mat6Name = mat6Name; }

    public String getMat6Target() { return mat6Target; }
    public void setMat6Target(String mat6Target) { this.mat6Target = mat6Target; }

    public String getMat6Setpoint() { return mat6Setpoint; }
    public void setMat6Setpoint(String mat6Setpoint) { this.mat6Setpoint = mat6Setpoint; }

    public String getMat6Achieved() { return mat6Achieved; }
    public void setMat6Achieved(String mat6Achieved) { this.mat6Achieved = mat6Achieved; }

    public String getMat7Name() { return mat7Name; }
    public void setMat7Name(String mat7Name) { this.mat7Name = mat7Name; }

    public String getMat7Target() { return mat7Target; }
    public void setMat7Target(String mat7Target) { this.mat7Target = mat7Target; }

    public String getMat7Setpoint() { return mat7Setpoint; }
    public void setMat7Setpoint(String mat7Setpoint) { this.mat7Setpoint = mat7Setpoint; }

    public String getMat7Achieved() { return mat7Achieved; }
    public void setMat7Achieved(String mat7Achieved) { this.mat7Achieved = mat7Achieved; }

    public String getMat8Name() { return mat8Name; }
    public void setMat8Name(String mat8Name) { this.mat8Name = mat8Name; }

    public String getMat8Target() { return mat8Target; }
    public void setMat8Target(String mat8Target) { this.mat8Target = mat8Target; }

    public String getMat8Setpoint() { return mat8Setpoint; }
    public void setMat8Setpoint(String mat8Setpoint) { this.mat8Setpoint = mat8Setpoint; }

    public String getMat8Achieved() { return mat8Achieved; }
    public void setMat8Achieved(String mat8Achieved) { this.mat8Achieved = mat8Achieved; }

    public String getMat9Name() { return mat9Name; }
    public void setMat9Name(String mat9Name) { this.mat9Name = mat9Name; }

    public String getMat9Target() { return mat9Target; }
    public void setMat9Target(String mat9Target) { this.mat9Target = mat9Target; }

    public String getMat9Setpoint() { return mat9Setpoint; }
    public void setMat9Setpoint(String mat9Setpoint) { this.mat9Setpoint = mat9Setpoint; }

    public String getMat9Achieved() { return mat9Achieved; }
    public void setMat9Achieved(String mat9Achieved) { this.mat9Achieved = mat9Achieved; }

    public String getMat10Name() { return mat10Name; }
    public void setMat10Name(String mat10Name) { this.mat10Name = mat10Name; }

    public String getMat10Target() { return mat10Target; }
    public void setMat10Target(String mat10Target) { this.mat10Target = mat10Target; }

    public String getMat10Setpoint() { return mat10Setpoint; }
    public void setMat10Setpoint(String mat10Setpoint) { this.mat10Setpoint = mat10Setpoint; }

    public String getMat10Achieved() { return mat10Achieved; }
    public void setMat10Achieved(String mat10Achieved) { this.mat10Achieved = mat10Achieved; }

    public String getMat11Name() { return mat11Name; }
    public void setMat11Name(String mat11Name) { this.mat11Name = mat11Name; }

    public String getMat11Target() { return mat11Target; }
    public void setMat11Target(String mat11Target) { this.mat11Target = mat11Target; }

    public String getMat11Setpoint() { return mat11Setpoint; }
    public void setMat11Setpoint(String mat11Setpoint) { this.mat11Setpoint = mat11Setpoint; }

    public String getMat11Achieved() { return mat11Achieved; }
    public void setMat11Achieved(String mat11Achieved) { this.mat11Achieved = mat11Achieved; }

    public String getMat12Name() { return mat12Name; }
    public void setMat12Name(String mat12Name) { this.mat12Name = mat12Name; }

    public String getMat12Target() { return mat12Target; }
    public void setMat12Target(String mat12Target) { this.mat12Target = mat12Target; }

    public String getMat12Setpoint() { return mat12Setpoint; }
    public void setMat12Setpoint(String mat12Setpoint) { this.mat12Setpoint = mat12Setpoint; }

    public String getMat12Achieved() { return mat12Achieved; }
    public void setMat12Achieved(String mat12Achieved) { this.mat12Achieved = mat12Achieved; }

    public String getMat13Name() { return mat13Name; }
    public void setMat13Name(String mat13Name) { this.mat13Name = mat13Name; }

    public String getMat13Target() { return mat13Target; }
    public void setMat13Target(String mat13Target) { this.mat13Target = mat13Target; }

    public String getMat13Setpoint() { return mat13Setpoint; }
    public void setMat13Setpoint(String mat13Setpoint) { this.mat13Setpoint = mat13Setpoint; }

    public String getMat13Achieved() { return mat13Achieved; }
    public void setMat13Achieved(String mat13Achieved) { this.mat13Achieved = mat13Achieved; }

    public String getMat14Name() { return mat14Name; }
    public void setMat14Name(String mat14Name) { this.mat14Name = mat14Name; }

    public String getMat14Target() { return mat14Target; }
    public void setMat14Target(String mat14Target) { this.mat14Target = mat14Target; }

    public String getMat14Setpoint() { return mat14Setpoint; }
    public void setMat14Setpoint(String mat14Setpoint) { this.mat14Setpoint = mat14Setpoint; }

    public String getMat14Achieved() { return mat14Achieved; }
    public void setMat14Achieved(String mat14Achieved) { this.mat14Achieved = mat14Achieved; }

    public String getMat15Name() { return mat15Name; }
    public void setMat15Name(String mat15Name) { this.mat15Name = mat15Name; }

    public String getMat15Target() { return mat15Target; }
    public void setMat15Target(String mat15Target) { this.mat15Target = mat15Target; }

    public String getMat15Setpoint() { return mat15Setpoint; }
    public void setMat15Setpoint(String mat15Setpoint) { this.mat15Setpoint = mat15Setpoint; }

    public String getMat15Achieved() { return mat15Achieved; }
    public void setMat15Achieved(String mat15Achieved) { this.mat15Achieved = mat15Achieved; }

    public String getMat16Name() { return mat16Name; }
    public void setMat16Name(String mat16Name) { this.mat16Name = mat16Name; }

    public String getMat16Target() { return mat16Target; }
    public void setMat16Target(String mat16Target) { this.mat16Target = mat16Target; }

    public String getMat16Setpoint() { return mat16Setpoint; }
    public void setMat16Setpoint(String mat16Setpoint) { this.mat16Setpoint = mat16Setpoint; }

    public String getMat16Achieved() { return mat16Achieved; }
    public void setMat16Achieved(String mat16Achieved) { this.mat16Achieved = mat16Achieved; }

    public String getMat17Name() { return mat17Name; }
    public void setMat17Name(String mat17Name) { this.mat17Name = mat17Name; }

    public String getMat17Target() { return mat17Target; }
    public void setMat17Target(String mat17Target) { this.mat17Target = mat17Target; }

    public String getMat17Setpoint() { return mat17Setpoint; }
    public void setMat17Setpoint(String mat17Setpoint) { this.mat17Setpoint = mat17Setpoint; }

    public String getMat17Achieved() { return mat17Achieved; }
    public void setMat17Achieved(String mat17Achieved) { this.mat17Achieved = mat17Achieved; }

    public String getMat18Name() { return mat18Name; }
    public void setMat18Name(String mat18Name) { this.mat18Name = mat18Name; }

    public String getMat18Target() { return mat18Target; }
    public void setMat18Target(String mat18Target) { this.mat18Target = mat18Target; }

    public String getMat18Setpoint() { return mat18Setpoint; }
    public void setMat18Setpoint(String mat18Setpoint) { this.mat18Setpoint = mat18Setpoint; }

    public String getMat18Achieved() { return mat18Achieved; }
    public void setMat18Achieved(String mat18Achieved) { this.mat18Achieved = mat18Achieved; }

    public String getMat19Name() { return mat19Name; }
    public void setMat19Name(String mat19Name) { this.mat19Name = mat19Name; }

    public String getMat19Target() { return mat19Target; }
    public void setMat19Target(String mat19Target) { this.mat19Target = mat19Target; }

    public String getMat19Setpoint() { return mat19Setpoint; }
    public void setMat19Setpoint(String mat19Setpoint) { this.mat19Setpoint = mat19Setpoint; }

    public String getMat19Achieved() { return mat19Achieved; }
    public void setMat19Achieved(String mat19Achieved) { this.mat19Achieved = mat19Achieved; }

    public String getMat20Name() { return mat20Name; }
    public void setMat20Name(String mat20Name) { this.mat20Name = mat20Name; }

    public String getMat20Target() { return mat20Target; }
    public void setMat20Target(String mat20Target) { this.mat20Target = mat20Target; }

    public String getMat20Setpoint() { return mat20Setpoint; }
    public void setMat20Setpoint(String mat20Setpoint) { this.mat20Setpoint = mat20Setpoint; }

    public String getMat20Achieved() { return mat20Achieved; }
    public void setMat20Achieved(String mat20Achieved) { this.mat20Achieved = mat20Achieved; }

    // --- Totals ---
    public String getTargetTotal() { return targetTotal; }
    public void setTargetTotal(String targetTotal) { this.targetTotal = targetTotal; }

    public String getSetpointTotal() { return setpointTotal; }
    public void setSetpointTotal(String setpointTotal) { this.setpointTotal = setpointTotal; }

    public String getAchievedTotal() { return achievedTotal; }
    public void setAchievedTotal(String achievedTotal) { this.achievedTotal = achievedTotal; }

    // --- Extras ---
    public String getCustomerAdd() { return customerAdd; }
    public void setCustomerAdd(String customerAdd) { this.customerAdd = customerAdd; }

    public String getSiteAdd() { return siteAdd; }
    public void setSiteAdd(String siteAdd) { this.siteAdd = siteAdd; }

    public java.sql.Timestamp getCreateDate() { return createDate; }
    public void setCreateDate(java.sql.Timestamp createDate) { this.createDate = createDate; }

}
