package com.example.mybalance.accounts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybalance.modelsDB.Accounts;

import java.util.List;

public class AccountsViewModel extends ViewModel {
private final MutableLiveData <List<Accounts>> accountsList = new MutableLiveData<>();

public void addAccount (Accounts account){
    List<Accounts> list = accountsList.getValue();
    list.add(account);
    accountsList.postValue(list);
}
    public LiveData<List<Accounts>> getAccountsList() {
        return accountsList;
    }

    public void setAccountsListOnUI(List<Accounts> list){
        accountsList.setValue(list);
    }
    public void setAccountsList(List<Accounts> list){
        accountsList.postValue(list);
    }
}
