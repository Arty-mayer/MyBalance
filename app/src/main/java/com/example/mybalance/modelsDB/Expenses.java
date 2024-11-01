package com.example.mybalance.modelsDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Expenses {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String description;
    public String date;
    public float amount;
    public String photo_file_name;
    public  int accountsId;



    public Expenses(String name, String description, String date, float amount, String photo_file_name) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.photo_file_name = photo_file_name;
    }

    public int getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(int accountsId) {
        this.accountsId = accountsId;
    }

    public Expenses() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPhoto_file_name() {
        return photo_file_name;
    }

    public void setPhoto_file_name(String photo_file_name) {
        this.photo_file_name = photo_file_name;
    }
}

