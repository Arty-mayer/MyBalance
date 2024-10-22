package com.example.mybalance.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpensesDao {
    @Insert
    void insert(Expenses expense);

    @Delete
    void delete(Expenses expense);

    @Update
    void updeate(Expenses expense);

    @Query("SELECT * FROM Expenses")
    List<Expenses> getAllExpenses();
}
