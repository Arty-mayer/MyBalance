package com.example.mybalance.modelsDB;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
       this.currencyId = currencyId;
    }

    public Accounts() {
    }


/*
    public Accounts(String name, String description, float amount, String lastIncomeDate, float lastIncomeAmount, String lastExpensesDate, float lastExpensesAmount) {

        this.name = name;
        this.description = description;
        this.amount = amount;
        this.lastIncomeDate = lastIncomeDate;
        this.lastIncomeAmount = lastIncomeAmount;
        this.lastExpensesDate = lastExpensesDate;
        this.lastExpensesAmount = lastExpensesAmount;
    }
*/
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
}

