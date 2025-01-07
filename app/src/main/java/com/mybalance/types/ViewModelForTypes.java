package com.mybalance.types;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mybalance.modelsDB.ExpenseType;
import com.mybalance.modelsDB.IncomeType;

import java.util.List;

public class ViewModelForTypes extends ViewModel {
    private MutableLiveData<List<IncomeType>> listInc = new MutableLiveData<>();
    private MutableLiveData<List<ExpenseType>> listExp = new MutableLiveData<>();

    public MutableLiveData<List<ExpenseType>> getListExp() {
        return listExp;
    }

    public void setListExp(List<ExpenseType> listExp) {
        this.listExp.postValue(listExp);
    }

    public MutableLiveData<List<IncomeType>> getListInc() {
        return listInc;
    }

    public void setListInc(List<IncomeType> listInc) {
        this.listInc.postValue(listInc);
    }
}
