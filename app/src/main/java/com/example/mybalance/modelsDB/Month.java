package com.example.mybalance.modelsDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Month {
@PrimaryKey(autoGenerate = true)
    public int id;
    public int year;
    public int month;
    public float incoming;
    public float expenses;
    public float balance;

    public Month() {
    }

    public Month(int year, int month, float incoming, float expenses, float balance) {
        this.year = year;
        this.month = month;
        this.incoming = incoming;
        this.expenses = expenses;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getIncoming() {
        return incoming;
    }

    public void setIncoming(float incoming) {
        this.incoming = incoming;
    }

    public float getExpenses() {
        return expenses;
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
