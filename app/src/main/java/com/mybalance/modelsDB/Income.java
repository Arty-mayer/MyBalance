package com.mybalance.modelsDB;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "date")})
public class Income {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private String date;
    private float amount;
    private String photo_file_name;
    private int accountsId;
    private long incomeTypeId;

    public Income() {
    }

    @Ignore
    public Income(String name, String description, String date, float amount, String photo_file_name) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.photo_file_name = photo_file_name;
    }

    public long getId() {
        return id;
    }

    public int getAccountsId() {
        return accountsId;
    }

    public long getIncomeTypeId() {
        return incomeTypeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public String getPhoto_file_name() {
        return photo_file_name;
    }


    public void setAccountsId(int accountsId) {
        this.accountsId = accountsId;
    }

    public void setIncomeTypeId(long incomeTypeId) {
        this.incomeTypeId = incomeTypeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setPhoto_file_name(String photo_file_name) {
        this.photo_file_name = photo_file_name;
    }
}

