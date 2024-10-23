package com.project.farmers.entity.weight;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;


@Entity(tableName = "weighing")
public class Weighing {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerName;
    private String farmerPhone;  // Add farmer phone field
    private String product;
    private double weight;
    private Date date;

    // Constructor with date parameter
    public Weighing(String farmerName, String farmerPhone, String product, double weight, Date date) {
        this.farmerName = farmerName;
        this.farmerPhone = farmerPhone;  // Add farmer phone to constructor
        this.product = product;
        this.weight = weight;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

    public String getFarmerPhone() { return farmerPhone; }  // Getter for phone number
    public void setFarmerPhone(String farmerPhone) { this.farmerPhone = farmerPhone; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
