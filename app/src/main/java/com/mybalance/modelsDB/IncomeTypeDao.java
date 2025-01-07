package com.mybalance.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IncomeTypeDao {
    @Insert
    long insert(IncomeType incomeType);

    @Delete
    void delete(IncomeType incomeType);

    @Update
    void update(IncomeType incomeType);

    @Query("SELECT * FROM IncomeType ORDER BY sortOrder")
    List<IncomeType> getAllIncomeTypes();

    @Query("SELECT * FROM IncomeType WHERE id = :id")
    IncomeType getIncomeTypeById(long id);

    @Query("SELECT MAX(sortOrder) FROM IncomeType")
    long getMaxSortOrder();

    @Query("UPDATE IncomeType SET sortOrder = sortOrder-1 WHERE sortOrder > :deletedPosition")
    void updateSortOrderForDelete(long deletedPosition);

    @Query("UPDATE IncomeType SET sortOrder = sortOrder-1 WHERE sortOrder > :altPosition AND sortOrder <= :newPosition")
    void updateSortOrderUp(long altPosition, long newPosition);

    @Query("UPDATE IncomeType SET sortOrder = sortOrder+1 WHERE sortOrder < :altPosition AND sortOrder >= :newPosition")
    void updateSortOrderDown(long altPosition, long newPosition);

    @Query("UPDATE IncomeType SET sortOrder = -1 WHERE sortOrder = :altPosition")
    void setSortOrderTemp(long altPosition);

    @Query("UPDATE IncomeType SET sortOrder = :newPosition WHERE sortOrder = -1")
    void setSortOrderNewPos(long newPosition);
}

