package com.mybalance.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mybalance.modelsDB.Accounts;

import java.util.List;

public class ViewModelForIncExpEditor extends ViewModel {
  MutableLiveData<List<Accounts>> listAccounts = new MutableLiveData<>();

    public LiveData<List<Accounts>> getListAccounts() {
        return listAccounts;
    }

    public void setListAccounts(List<Accounts> listAccounts) {
        this.listAccounts.postValue(listAccounts);
    }
    public void setListAccountsOnUI(List<Accounts> listAccounts) {
        this.listAccounts.setValue(listAccounts);
    }
}
