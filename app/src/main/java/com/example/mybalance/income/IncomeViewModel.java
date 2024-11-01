package com.example.mybalance.income;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Income;

import java.util.List;

public class IncomeViewModel extends ViewModel {
    MutableLiveData<List<Income>> listIncome = new MutableLiveData<>();
    MutableLiveData<List<Accounts>> listAccounts = new MutableLiveData<>();

    public LiveData<List<Income>> getListIncome (){
        return listIncome;
    }
    public void setListIncome (List<Income> list) {
        listIncome.postValue(list);
    }
    public void setListIncomeOnUi (List<Income> list) {
        listIncome.setValue(list);
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
