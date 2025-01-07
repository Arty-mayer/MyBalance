package com.mybalance.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MonthDao {
    @Insert
    void insert(Month month);

    @Delete
    void delete(Month month);

    @Update
    void updeate(Month month);

    @Query("SELECT * FROM Month")
    List<Month> getAllMonth();
}
