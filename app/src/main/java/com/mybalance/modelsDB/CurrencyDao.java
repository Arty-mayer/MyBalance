package com.mybalance.modelsDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Insert
    void insert(Currency currency);
/*
    @Delete
    void delete(Currency currency);

    @Update
    void updeate(Currency currency);
*/
    @Query("SELECT * FROM Currency")
    List<Currency> getAllCurrency();

    @Query("SELECT * FROM Currency WHERE code = :code LIMIT 1")
    Currency getByCode (int code);

    @Query("SELECT * FROM Currency WHERE char_code = :char_code LIMIT 1")
    Currency getByCharCode (String char_code);

    @Query("SELECT * FROM Currency WHERE id = :id")
    Currency getById (int id);

}
