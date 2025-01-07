package com.example.mybalance.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseTypeDao {
    @Insert
    long insert(ExpenseType expenseType);

    @Delete
    void delete(ExpenseType expenseType);

    @Update
    void update(ExpenseType expenseType);

    @Query("SELECT * FROM ExpenseType ORDER BY sortOrder")
    List<ExpenseType> getAllExpenseTypes();

    @Query("SELECT * FROM ExpenseType WHERE id = :id")
    ExpenseType getExpenseExpenseTypeById(long id);

    @Query("SELECT MAX(sortOrder) FROM ExpenseType")
    long getMaxSortOrder();

    @Query("UPDATE ExpenseType SET sortOrder = sortOrder-1 WHERE sortOrder > :deletedPosition")
    void updateSortOrderForDelete(long deletedPosition);

    @Query("UPDATE ExpenseType SET sortOrder = sortOrder-1 WHERE sortOrder > :altPosition AND sortOrder <= :newPosition")
    void updateSortOrderUp(long altPosition, long newPosition);

    @Query("UPDATE ExpenseType SET sortOrder = sortOrder+1 WHERE sortOrder < :altPosition AND sortOrder >= :newPosition")
    void updateSortOrderDown(long altPosition, long newPosition);

    @Query("UPDATE ExpenseType SET sortOrder = -1 WHERE sortOrder = :altPosition")
    void setSortOrderTemp(long altPosition);

    @Query("UPDATE ExpenseType SET sortOrder = :newPosition WHERE sortOrder = -1")
    void setSortOrderNewPos(long newPosition);
}

