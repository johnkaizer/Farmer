package com.project.farmers.entity.farmers;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FarmerDao {

    @Insert
    void insert(Farmer farmer);

    @Query("SELECT * FROM farmers WHERE phone = :phone LIMIT 1")
    Farmer getFarmerByPhone(String phone);

    @Query("SELECT * FROM farmers")
    List<Farmer> getAllFarmers();

    @Update
    void update(Farmer farmer);

    @Delete
    void delete(Farmer farmer);
    @Query("SELECT COUNT(*) FROM farmers")
    int getFarmersCount();


    @Query("DELETE FROM farmers WHERE id = :farmerId")
    void deleteById(int farmerId);

    @Query("SELECT * FROM farmers WHERE name = :farmerName")
    Farmer getFarmerByName(String farmerName);
}
