package com.mybalance.modelsDB;

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
    void update(Expenses expense);

    @Query("SELECT * FROM Expenses")
    List<Expenses> getAllExpenses();

    @Query("SELECT * FROM Expenses WHERE id = :id")
    Expenses getExpenseById(long id);

    @Query("SELECT * FROM Expenses WHERE accountsId = :id ORDER BY date")
    List<Expenses> getExpensesByAccountsId(int id);

    @Query("SELECT * FROM Expenses WHERE date BETWEEN :date1 AND :date2 ORDER BY date")
    List<Expenses> getExpensesRange(String date1, String date2);

    @Query("SELECT * FROM Expenses WHERE accountsId = :id AND date BETWEEN :date1 AND :date2 ORDER BY date")
    List<Expenses> getExpensesRangeByAccount(String date1, String date2, int id);
}
