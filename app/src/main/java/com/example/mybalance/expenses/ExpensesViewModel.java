package com.example.mybalance.expenses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Expenses;
import com.example.mybalance.modelsDB.Income;

import java.util.List;

public class ExpensesViewModel extends ViewModel {
    MutableLiveData<List<Expenses>> listExpenses = new MutableLiveData<>();
    MutableLiveData<List<Accounts>> listAccounts = new MutableLiveData<>();

    public LiveData<List<Expenses>> getListExpenses (){
        return listExpenses;
    }

    public void setListExpenses (List<Expenses> list) {
        listExpenses.postValue(list);
    }
    public void setListExpensesOnUi (List<Expenses> list) {
        listExpenses.setValue(list);
    }


    public LiveData<List<Accounts>> getListAccounts() {
        return listAccounts;
    }

    public void setListAccounts(List<Accounts> list) {
        listAccounts.postValue(list);
    }
    public void setListAccountsOnUi(List<Accounts> list) {
        listAccounts.setValue(list);
    }
}
