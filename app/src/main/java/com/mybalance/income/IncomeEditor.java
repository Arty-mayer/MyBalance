package com.mybalance.income;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.mybalance.common.CommonEditorIncExp;
import com.mybalance.data.AppDB;
import com.mybalance.modelsDB.AccountsDao;


import com.mybalance.modelsDB.Income;
import com.mybalance.modelsDB.IncomeDao;
import com.mybalance.modelsDB.IncomeType;
import com.mybalance.modelsDB.IncomeTypeDao;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

public class IncomeEditor extends CommonEditorIncExp {

    ArrayAdapter<IncomeType> typesAdapter;
    Income income;

    @Override
    protected void createTypesAdapter() {
        typesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryField.setAdapter(typesAdapter);
    }

    @Override
    protected void onItemSelectedCategoryField(AdapterView<?> parent, View view, int position, long id) {
        if (typesAdapter.getItem(position) != null && typesAdapter.getItem(position).getId() != 0) {
            typeId = typesAdapter.getItem(position).getId();
            dataChangedNotify();
        }
    }

    @Override
    protected void onTextChangedAmountField(CharSequence s, int start, int before, int count) {
        if (Float.parseFloat(amountField.getText().toString()) != income.getAmount()) {
            dataChangedNotify();
        }
    }

    @Override
    protected void onTextChangedNameField(CharSequence s, int start, int before, int count) {
        String input = (nameField.getText() == null) ? "" : nameField.getText().toString();
        String inDb = (income.getName() == null) ? "" : income.getName();
        if (inDb.compareTo(input) != 0) {
            dataChangedNotify();
        }
    }

    @Override
    protected void onDateChangedDateEdit(int day, int month, int year) {
        LocalDate date = LocalDate.parse(income.getDate());
        if (date.getDayOfMonth() != day || date.getMonthValue() != month || date.getYear() != year) {
            dataChangedNotify();
        }
    }

    protected void saveDataInDb() {
        db = AppDB.getDb(this);
        float amount = income.getAmount();
        try {
            amount = Float.parseFloat(amountField.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "parseFloat error", Toast.LENGTH_SHORT).show();
        }
        income.setAmount(amount);
        income.setName(nameField.getText().toString());
        income.setIncomeTypeId(typeId);
        String date = dateEdit.getDate();
        if (date != null) {
            income.setDate(date);
        }
        income.setAccountsId(accountId);
        IncomeDao incomeDao = db.incomeDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                incomeDao.update(income);
            }
        });
    }

    protected void loadDataFromDb() {
        db = AppDB.getDb(this);
        IncomeDao incomeDao = db.incomeDao();
        AccountsDao accountsDao = db.accountsDao();
        IncomeTypeDao incomeTypeDao = db.incomeTypeDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                income = incomeDao.getIncomeById(id);
                List<IncomeType> incomeTypeList = incomeTypeDao.getAllIncomeTypes();
                typesAdapter.clear();
                typesAdapter.addAll(incomeTypeList);
                viewModel.setListAccounts(accountsDao.getAllAccounts());
            }
        });
    }

    protected void setDataInInterface() {
        accountId = income.getAccountsId();
        typeId = income.getIncomeTypeId();
        setSelectedAccUndType();
        accountsAdapter.notifyDataSetChanged();
        typesAdapter.notifyDataSetChanged();
        amountField.setText(String.valueOf(income.getAmount()));
        nameField.setText(income.getName());
        dateEdit.setDateStr(income.getDate());
    }

    protected void setSelectedAccUndType() {
        for (int i = 0; i < accountsAdapter.getCount(); i++) {
            if (income.getAccountsId() == accountsAdapter.getItem(i).getId()) {
                accountsSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < typesAdapter.getCount(); i++) {
            if (typesAdapter.getItem(i).getId() == income.getIncomeTypeId()) {
                IncomeType type = typesAdapter.getItem(i);
                if (type != null && type.getName() != null) {
                    categoryField.setText(type.getName());
                }
                break;
            }
        }
    }
}
