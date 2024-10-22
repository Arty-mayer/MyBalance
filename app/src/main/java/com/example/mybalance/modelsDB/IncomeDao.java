package com.example.mybalance.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IncomeDao {
    @Insert
    void insert(Income income);

    @Delete
    void delete(Income income);

    @Update
    void updeate(Income income);

    @Query("SELECT * FROM Income")
    List<Income> getAllIncome();
}
