package com.project.farmers.entity.weight;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.farmers.entity.farmers.Farmer;
import com.project.farmers.entity.farmers.FarmerDao;
import com.project.farmers.util.DateConverter;

import androidx.room.TypeConverters;
@Database(entities = {Farmer.class, Weighing.class}, version = 3)
@TypeConverters({DateConverter.class})  // Add the DateConverter
public abstract class WeighingDatabase extends RoomDatabase {
    private static WeighingDatabase instance;

    public abstract FarmerDao farmerDao();
    public abstract WeighingDao weighingDao();

    public static synchronized WeighingDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            WeighingDatabase.class, "weighing_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()  // Consider removing this in production
                    .build();
        }
        return instance;
    }
}
