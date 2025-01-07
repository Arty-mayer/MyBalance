package com.example.mybalance.common;

import android.app.DatePickerDialog;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.mybalance.R;
import com.example.mybalance.utils.EditDate;
import com.example.mybalance.data.AppDB;
import com.example.mybalance.modelsDB.Accounts;


import java.util.Calendar;

import java.util.List;

public abstract class CommonEditorIncExp extends AppCompatActivity {

    protected Button exitButton;
    protected Button saveButton;
    protected ImageButton calendarButton;
    protected EditText amountField;
    protected EditText nameField;
    protected EditDate dateEdit;
    protected AutoCompleteTextView categoryField;
    protected Spinner accountsSpinner;
    protected DatePickerDialog datePickerDialog;

    protected AppDB db;
    protected boolean firstInit = true;

    //data
    protected long id;

    protected long typeId;
    protected int accountId;

    protected ArrayAdapter<Accounts> accountsAdapter;
    // ArrayAdapter<ExpenseType> typesAdapter;

    //Expenses expense;

    protected ViewModelForIncExpEditor viewModel;

    protected abstract void onItemSelectedCategoryField(AdapterView<?> parent, View view, int position, long id);

    protected abstract void onTextChangedAmountField(CharSequence s, int start, int before, int count);

    protected abstract void onTextChangedNameField(CharSequence s, int start, int before, int count);

    protected abstract void onDateChangedDateEdit(int day, int month, int year);

    protected abstract void saveDataInDb();

    protected abstract void loadDataFromDb();
    protected abstract void setDataInInterface();
    protected abstract void setSelectedAccUndType();
    protected abstract void createTypesAdapter();
    //  typesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    //  typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // categoryField.setAdapter(typesAdapter);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_inc_editor);
        Intent intentParent = getIntent();
        id = intentParent.getLongExtra("Id", 0);
        if (id == 0) {
            Toast.makeText(this, "Запись не существует", Toast.LENGTH_LONG).show();
            finish();
        }
        findInterfaceItems();
        init();
        setListeners();
        loadDataFromDb();
        setSelectedAccUndType();
    }

    protected void init() {
        viewModel = new ViewModelForIncExpEditor();
        datePickerDialog = new DatePickerDialog(this);
        accountsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountsSpinner.setAdapter(accountsAdapter);
        createTypesAdapter();
    }

    private void findInterfaceItems() {
        exitButton = findViewById(R.id.closeButton);
        saveButton = findViewById(R.id.saveButton);
        calendarButton = findViewById(R.id.calendarBtn);
        amountField = findViewById(R.id.editTextAmount);
        nameField = findViewById(R.id.editTextName);
        categoryField = findViewById(R.id.expenseType);
        dateEdit = findViewById(R.id.date);
        accountsSpinner = findViewById(R.id.accountsSpinner);
    }

    protected void setListeners() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.requestFocus();
                saveDataInDb();
                clearFocus();
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateEdit.getDay() != 0 && dateEdit.getMonth() != 0 && dateEdit.getYear() != 0) {
                    datePickerDialog.updateDate(dateEdit.getYear(), dateEdit.getMonth() - 1, dateEdit.getDay());
                } else {
                    Calendar calendar = Calendar.getInstance();
                    datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                }
                datePickerDialog.show();
            }
        });

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateEdit.setDateInt(dayOfMonth, month + 1, year);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        categoryField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {categoryField.showDropDown();
                }
            }
        });

        categoryField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedCategoryField(parent, view, position, id);
            }
        });

        accountsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (accountsAdapter.getItem(position) != null && accountsAdapter.getItem(position).getId() != 0) {
                    accountId = accountsAdapter.getItem(position).getId();
                    if (!firstInit) {
                        dataChangedNotify();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChangedAmountField(s, start, before, count);
                /*
                if (Float.parseFloat(amountField.getText().toString()) != expense.getAmount()) {
                    dataChangedNotify();
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChangedNameField(s, start, before, count);
                /*
                String input = (nameField.getText() == null) ? "" : nameField.getText().toString();
                String inDb = (expense.getName() == null) ? "" : expense.getName();
                if (inDb.compareTo(input) != 0) {
                    dataChangedNotify();
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateEdit.addOnDateChangeListener(new EditDate.OnDateChange() {
            @Override
            public void onChange(int day, int month, int year) {
                onDateChangedDateEdit(day, month, year);
                /*
                LocalDate date = LocalDate.parse(expense.getDate());
                if (date.getDayOfMonth() != day || date.getMonthValue() != month || date.getYear() != year) {
                    dataChangedNotify();
                }*/
            }
        });


        viewModel.getListAccounts().observe(this, new Observer<List<Accounts>>() {
            @Override
            public void onChanged(List<Accounts> accountsList) {
                accountsAdapter.clear();
                accountsAdapter.addAll(accountsList);
                setDataInInterface();
            }

        });
    }

    protected void dataChangedNotify() {
        saveButton.setEnabled(true);
    }

    private void clearFocus() {
        nameField.clearFocus();
        amountField.clearFocus();
        categoryField.clearFocus();
        dateEdit.clearFocus();
        accountsSpinner.clearFocus();
    }

    /*
    private void saveDataInDb() {
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
    }*/
/*
    private void loadDataFromDb() {
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

    }*/
/*
    private void setDataInInterface() {
        accountId = expense.getAccountsId();
        typeId = expense.getExpenseTypeId();
        setSelectedAccUndType();
        accountsAdapter.notifyDataSetChanged();
        typesAdapter.notifyDataSetChanged();
        amountField.setText(String.valueOf(expense.getAmount()));
        nameField.setText(expense.getName());
        dateEdit.setDateStr(expense.getDate());


    }*/
/*
    private void setSelectedAccUndType() {
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
    }*/
}
