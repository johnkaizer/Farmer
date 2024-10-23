package com.project.farmers.entity.weight;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeighingDao {
    @Query("SELECT * FROM weighing")
    List<Weighing> getAllWeighings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weighing weighing);

    @Query("SELECT COUNT(*) FROM weighing")
    int getWeighingsCount();

}

