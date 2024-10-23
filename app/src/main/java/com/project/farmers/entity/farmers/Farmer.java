package com.project.farmers.entity.farmers;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "farmers")
public class Farmer {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String phone;

    // Constructors, getters, and setters
    public Farmer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
