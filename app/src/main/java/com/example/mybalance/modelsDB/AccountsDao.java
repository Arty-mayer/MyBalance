package com.example.mybalance.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountsDao {
    @Insert
    void insert(Accounts account);

    @Delete
    void delete(Accounts account);

    @Update
    void updeate(Accounts account);

    @Query("SELECT * FROM Accounts")
    List<Accounts> getAllAccounts();

    @Query("SELECT * FROM Accounts WHERE id = :id")
    Accounts getAccount(int id);
}