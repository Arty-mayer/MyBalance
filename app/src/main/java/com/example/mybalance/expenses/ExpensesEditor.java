package com.example.mybalance.expenses;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.mybalance.common.CommonEditorIncExp;
import com.example.mybalance.data.AppDB;
import com.example.mybalance.modelsDB.AccountsDao;
import com.example.mybalance.modelsDB.ExpenseType;
import com.example.mybalance.modelsDB.ExpenseTypeDao;
import com.example.mybalance.modelsDB.Expenses;
import com.example.mybalance.modelsDB.ExpensesDao;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

public class ExpensesEditor extends CommonEditorIncExp {

    ArrayAdapter<ExpenseType> typesAdapter;
    Expenses expense;

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
        if (Float.parseFloat(amountField.getText().toString()) != expense.getAmount()) {
            dataChangedNotify();
        }
    }

    @Override
    protected void onTextChangedNameField(CharSequence s, int start, int before, int count) {
        String input = (nameField.getText() == null) ? "" : nameField.getText().toString();
        String inDb = (expense.getName() == null) ? "" : expense.getName();
        if (inDb.compareTo(input) != 0) {
            dataChangedNotify();
        }
    }

    @Override
    protected void onDateChangedDateEdit(int day, int month, int year) {
        LocalDate date = LocalDate.parse(expense.getDate());
        if (date.getDayOfMonth() != day || date.getMonthValue() != month || date.getYear() != year) {
            dataChangedNotify();
        }
    }

    protected void saveDataInDb() {
        db = AppDB.getDb(this);
        float amount = expense.getAmount();
        try {
            amount = Float.parseFloat(amountField.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "parseFloat error", Toast.LENGTH_SHORT).show();
        }
        expense.setAmount(amount);
        expense.setName(nameField.getText().toString());
        expense.setExpenseTypeId(typeId);
        String date = dateEdit.getDate();
        if (date != null) {
            expense.setDate(date);
        }
        expense.setAccountsId(accountId);
        ExpensesDao expenseDao = db.expensesDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                expenseDao.update(expense);
            }
        });
    }

    protected void loadDataFromDb() {
        db = AppDB.getDb(this);
        ExpensesDao expenseDao = db.expensesDao();
        AccountsDao accountsDao = db.accountsDao();
        ExpenseTypeDao expenseTypeDao = db.expenseTypeDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                expense = expenseDao.getExpenseById(id);
                List<ExpenseType> expenseTypeList = expenseTypeDao.getAllExpenseTypes();
                typesAdapter.clear();
                typesAdapter.addAll(expenseTypeList);
                viewModel.setListAccounts(accountsDao.getAllAccounts());
            }
        });

    }

    protected void setDataInInterface() {
        accountId = expense.getAccountsId();
        typeId = expense.getExpenseTypeId();
        setSelectedAccUndType();
        accountsAdapter.notifyDataSetChanged();
        typesAdapter.notifyDataSetChanged();
        amountField.setText(String.valueOf(expense.getAmount()));
        nameField.setText(expense.getName());
        dateEdit.setDateStr(expense.getDate());


    }

    protected void setSelectedAccUndType() {
        for (int i = 0; i < accountsAdapter.getCount(); i++) {
            if (expense.getAccountsId() == accountsAdapter.getItem(i).getId()) {
                accountsSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < typesAdapter.getCount(); i++) {
            if (typesAdapter.getItem(i).getId() == expense.getExpenseTypeId()) {
                ExpenseType type = typesAdapter.getItem(i);
                if (type != null && type.getName() != null) {
                    categoryField.setText(type.getName());
                }
                break;
            }
        }
    }
}
