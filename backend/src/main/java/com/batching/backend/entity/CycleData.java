package com.batching.backend.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cycle_data")
public class CycleData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sr_no")
    private int srNo;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "material_1")
    private String material1;

    @Column(name = "material_2")
    private String material2;

    @Column(name = "material_3")
    private String material3;

    @Column(name = "material_4")
    private String material4;

    @Column(name = "material_5")
    private String material5;

    @Column(name = "material_6")
    private String material6;

    @Column(name = "material_7")
    private String material7;

    @Column(name = "material_8")
    private String material8;

    @Column(name = "material_9")
    private String material9;

    @Column(name = "material_10")
    private String material10;

    @Column(name = "material_11")
    private String material11;

    @Column(name = "material_12")
    private String material12;

    @Column(name = "material_13")
    private String material13;

    @Column(name = "material_14")
    private String material14;

    @Column(name = "material_15")
    private String material15;

    @Column(name = "material_16")
    private String material16;

    @Column(name = "material_17")
    private String material17;

    @Column(name = "material_18")
    private String material18;

    @Column(name = "material_19")
    private String material19;

    @Column(name = "material_20")
    private String material20;
}

