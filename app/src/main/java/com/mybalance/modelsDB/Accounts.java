package com.mybalance.modelsDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Accounts {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public float amount;
    public String lastIncomeDate;
    public float lastIncomeAmount;
    public String lastExpensesDate;
    public float lastExpensesAmount;
    public int currencyId;
    public String currencyCharCode;
    public String currencySymbol;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyCharCode() {
        return currencyCharCode;
    }

    public void setCurrencyCharCode(String currencyCharCode) {
        this.currencyCharCode = currencyCharCode;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public Accounts() {
    }

    @NotNull
    public int getId() {
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getLastIncomeDate() {
        return lastIncomeDate;
    }

    public void setLastIncomeDate(String lastIncomeDate) {
        this.lastIncomeDate = lastIncomeDate;
    }

    public float getLastIncomeAmount() {
        return lastIncomeAmount;
    }

    public void setLastIncomeAmount(float lastIncomeAmount) {
        this.lastIncomeAmount = lastIncomeAmount;
    }

    public String getLastExpensesDate() {
        return lastExpensesDate;
    }

    public void setLastExpensesDate(String lastExpensesDate) {
        this.lastExpensesDate = lastExpensesDate;
    }

    public float getLastExpensesAmount() {
        return lastExpensesAmount;
    }

    public void setLastExpensesAmount(float lastExpensesAmount) {
        this.lastExpensesAmount = lastExpensesAmount;
    }

    @Override
    public String toString (){
        return name;
    }
}

