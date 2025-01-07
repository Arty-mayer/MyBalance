package com.mybalance.modelsDB;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity(indices = {@Index(value = "date")})
public class Expenses {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private String date;
    private float amount;
    private String photo_file_name;
    private int accountsId;
    private long expenseTypeId;

    public Expenses() {
    }

    @Ignore
    public Expenses(String name, String description, String date, float amount, String photo_file_name) {
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

    public long getExpenseTypeId() {
        return expenseTypeId;
    }

    @Nullable
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

    public void setExpenseTypeId(long expenseTypeId) {
        this.expenseTypeId = expenseTypeId;
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
        if (isValidDate(date)) {
            this.date = date;
        } else {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setPhoto_file_name(String photo_file_name) {
        this.photo_file_name = photo_file_name;
    }

    @Ignore
    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

