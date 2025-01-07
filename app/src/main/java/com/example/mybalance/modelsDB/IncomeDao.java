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
    void update(Income income);

    @Query("SELECT * FROM Income")
    List<Income> getAllIncome();

    @Query("SELECT * FROM Income WHERE id = :id")
    Income getIncomeById(long id);

    @Query("SELECT * FROM Income WHERE accountsId = :id ORDER BY date")
    List<Income> getIncomesByAccountsId(int id);

    @Query("SELECT * FROM Income WHERE date BETWEEN :date1 AND :date2 ORDER BY date")
    List<Income> getIncomesRange(String date1, String date2);

    @Query("SELECT * FROM Income WHERE accountsId = :id AND date BETWEEN :date1 AND :date2 ORDER BY date")
    List<Income> getIncomesRangeByAccount(String date1, String date2, int id);
}
