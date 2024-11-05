package com.example.mybalance;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.Utils.AccountsDatesFotIncAdapter;
import com.example.mybalance.Utils.Constante;
import com.example.mybalance.Utils.PickerCustom;
import com.example.mybalance.data.AppDB;
import com.example.mybalance.expenses.AdapterForExpenses;
import com.example.mybalance.expenses.ExpensesViewModel;
import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.AccountsDao;
import com.example.mybalance.modelsDB.Expenses;
import com.example.mybalance.modelsDB.ExpensesDao;



import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;


public class FragmentExpenses extends Fragment {
    Button buttonPlus;
    Button buttonDate1;
    Button buttonDate2;
    Button buttonDate;
    Button buttonAdd;
    EditText editTextAmount;
    Spinner spinnerAccounts;
    TextView notice1;
    TextView notice2;
    TextView minus;
    RecyclerView expensesView;
    Switch swAllAccts;

    ExpensesViewModel expensesViewModel;
    ArrayAdapter<String> adapterForSpinner = null;
    AdapterForExpenses adapterForExpenses = null;

    SharedPreferences appPreferences;
    Calendar calendar;
    PickerCustom datePicker;

    // db
    ExpensesDao expensesDao = null;
    AccountsDao accountsDao = null;
    List<Accounts> accountsList = null;

    LocalDate dateForNewInc;
    LocalDate date1;
    LocalDate date2;
    int accountId = 0;
    Accounts account;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expenses_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findInterfaceItems(view);
        initialisation();
        setButtonPlusSymbol();
        setListeners();
        setObservers();
        getAccountsFromDb();


    }

    private void setObservers() {
        expensesViewModel.getListAccounts().observe(getViewLifecycleOwner(), new Observer<List<Accounts>>() {
            @Override
            public void onChanged(List<Accounts> accounts) {
                List<String> items = new ArrayList<>();
                int selectedPosition = -1;
                int id = appPreferences.getInt(Constante.defAccIdName, Constante.defAccIdValue);

                for (Accounts a : accounts) {
                    items.add(a.name);
                    if (id == a.getId()) {
                        selectedPosition = items.size() - 1;
                    }
                }
                adapterForSpinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
                adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAccounts.setAdapter(adapterForSpinner);
                spinnerAccounts.setSelection(selectedPosition);

                getExpensesFromDb();
            }
        });
        expensesViewModel.getListExpenses().observe(getViewLifecycleOwner(), new Observer<List<Expenses>>() {
            @Override
            public void onChanged(List<Expenses> expenses) {
                adapterForExpenses.setAccountsList(makeListAccDatesForExpAdapter(expenses));
                adapterForExpenses.updateList(expenses);
                int position = adapterForExpenses.getItemCount() - 1;
                if (position > 0) {
                    expensesView.smoothScrollToPosition(adapterForExpenses.getItemCount() - 1);
                }
            }
        });
    }

    private List<AccountsDatesFotIncAdapter> makeListAccDatesForExpAdapter(List<Expenses> expenses){
        List<AccountsDatesFotIncAdapter> accountsDate = new ArrayList<AccountsDatesFotIncAdapter>();
        for (Expenses expense: expenses) {
            for (Accounts a: expensesViewModel.getListAccounts().getValue()){
                if (a.getId() == expense.getAccountsId()){
                    accountsDate.add(new AccountsDatesFotIncAdapter(a.getName(), a.getCurrencySymbol(),a.getCurrencyCharCode()));
                    break;
                }
            }
        }
        return accountsDate;
    }

    private void getAccountsFromDb() {
        AppDB db = AppDB.getDb(getContext());
        if (accountsDao == null) {
            accountsDao = db.accountsDao();
        }
        if (expensesDao == null) {
            expensesDao = db.expensesDao();
        }
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                expensesViewModel.setListAccounts(accountsDao.getAllAccounts());
            }
        });
    }

    private void getExpensesFromDb() {

        AppDB db = AppDB.getDb(getContext());
        if (accountsDao == null) {
            accountsDao = db.accountsDao();
        }
        if (expensesDao == null) {
            expensesDao = db.expensesDao();
        }
        Executors.newSingleThreadExecutor().execute(() -> {

            if (swAllAccts.isChecked()) {
                adapterForExpenses.setPrintAccount(true);
                expensesViewModel.setListExpenses(expensesDao.getExpensesRange(date1.toString(), date2.toString()));
            } else if (accountId > 0) {
                adapterForExpenses.setPrintAccount(false);
                account = accountsDao.getAccount(accountId);
                expensesViewModel.setListExpenses(expensesDao.getExpensesRangeByAccount(date1.toString(), date2.toString(), accountId));
            }
        });
    }

    private void initialisation() {
        expensesViewModel = new ExpensesViewModel();
        appPreferences = this.getActivity().getSharedPreferences(Constante.preferences, getActivity().MODE_PRIVATE);
        calendar = Calendar.getInstance();
        dateForNewInc = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));

        buttonDate.setText(String.valueOf(dateForNewInc));
        YearMonth yearMonth = YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        date2 = yearMonth.atEndOfMonth();
        date1 = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);

        buttonDate1.setText(String.valueOf(date1));
        buttonDate2.setText(String.valueOf(date2));

        datePicker = new PickerCustom();
        adapterForExpenses = new AdapterForExpenses();
        expensesView.setLayoutManager(new LinearLayoutManager(getContext()));
        expensesView.setAdapter(adapterForExpenses);
        accountId = appPreferences.getInt(Constante.defAccIdName, Constante.defAccIdValue);
    }

    private void findInterfaceItems(View view) {
        buttonPlus = view.findViewById(R.id.plusButton);
        buttonDate1 = view.findViewById(R.id.date1);
        buttonDate2 = view.findViewById(R.id.date2);
        // buttonApply = view.findViewById(R.id.buttonApply);
        buttonDate = view.findViewById(R.id.date);
        buttonAdd = view.findViewById(R.id.addButton);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        spinnerAccounts = view.findViewById(R.id.mySpinner);
        notice1 = view.findViewById(R.id.incomeNotice1);
        notice2 = view.findViewById(R.id.incomeNotice2);
        minus = view.findViewById(R.id.textViewMinus);
        expensesView = view.findViewById(R.id.recyclerViewIncome);
        swAllAccts = view.findViewById(R.id.swAllAccounts);
    }

    private void setButtonPlusSymbol() {
        if (editTextAmount.getVisibility() == View.GONE) {
            buttonPlus.setText("+");
        } else {
            buttonPlus.setText("-");
        }
    }

    private void setListeners() {
        buttonPlus.setOnClickListener(v -> {
            if (editTextAmount.getVisibility() == View.GONE) {
                setFilterVisibility(false);
                setAddFormVisibility(true);
                setButtonPlusSymbol();

            } else {
                setAddFormVisibility(false);
                setFilterVisibility(true);
                setButtonPlusSymbol();
            }
        });

        buttonDate.setOnClickListener(v -> {
            datePicker.button = (Button) v;
            datePicker.dateKey = 0;
            datePicker.show(getActivity().getSupportFragmentManager(), "Picker_expense_1");
        });

        buttonDate1.setOnClickListener(v -> {
            datePicker.button = (Button) v;
            datePicker.dateKey = 1;
            datePicker.show(getActivity().getSupportFragmentManager(), "Picker_expense_1");
        });

        buttonDate2.setOnClickListener(v -> {
            datePicker.button = (Button) v;
            datePicker.dateKey = 2;
            datePicker.show(getActivity().getSupportFragmentManager(), "Picker_expense_1");
        });

        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    buttonAdd.setEnabled(true);
                } else {
                    buttonAdd.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonAdd.setOnClickListener(v -> {
            addInDb();
            editTextAmount.setText("");
            editTextAmount.clearFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
        });

        datePicker.setOnSelectedListener(new PickerCustom.OnSelectedListener() {
            @Override
            public void onDateSelected(LocalDate date) {
                switch (datePicker.dateKey) {
                    case 0:
                        dateForNewInc = date;
                        buttonDate.setText(String.valueOf(dateForNewInc));
                        break;
                    case 1:
                        date1 = date;
                        buttonDate1.setText(String.valueOf(date1));
                        checkRange(false);
                        break;
                    case 2:
                        date2 = date;
                        buttonDate2.setText(String.valueOf(date2));
                        checkRange(true);
                        break;
                }
                getExpensesFromDb();
            }
        });

        swAllAccts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerAccounts.setEnabled(!isChecked);
                getExpensesFromDb();
            }
        });

        spinnerAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                account = expensesViewModel.getListAccounts().getValue().get(position);
                accountId = account.getId();
                getExpensesFromDb();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void addInDb() {
        float amount;
        try {
            amount = Float.parseFloat(editTextAmount.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Expenses expense = new Expenses();
                expense.setAmount(amount);
                expense.setDate(dateForNewInc.toString());
                expense.setAccountsId(accountId);
                expensesDao.insert(expense);
                float newAmount = account.getAmount() - amount;
                account.setAmount(newAmount);
                accountsDao.update(account);
                expensesViewModel.setListExpenses(expensesDao.getExpensesRangeByAccount(date1.toString(), date2.toString(), accountId));
            }
        });
    }


    private void setFilterVisibility(boolean visible) {
        if (visible) {
            notice2.setVisibility(View.VISIBLE);
            minus.setVisibility(View.VISIBLE);
            buttonDate1.setVisibility(View.VISIBLE);
            buttonDate2.setVisibility(View.VISIBLE);
            //  buttonApply.setVisibility(View.VISIBLE);
              swAllAccts.setVisibility(View.VISIBLE);

        } else {
            notice2.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
            buttonDate1.setVisibility(View.GONE);
            buttonDate2.setVisibility(View.GONE);
            // buttonApply.setVisibility(View.GONE);
             swAllAccts.setVisibility(View.GONE);
        }
    }

    private void setAddFormVisibility(boolean visible) {
        if (visible) {
            // notice1.setVisibility(View.VISIBLE);
            buttonDate.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.VISIBLE);
            editTextAmount.setVisibility(View.VISIBLE);

            if (!spinnerAccounts.isEnabled()) {
                spinnerAccounts.setEnabled(true);
            }
        } else {
            //  notice1.setVisibility(View.GONE);
            buttonDate.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.GONE);
            editTextAmount.setVisibility(View.GONE);
            if (swAllAccts.isChecked()) {
                spinnerAccounts.setEnabled(false);
            }
        }
    }

    private void checkRange(Boolean setDate1) {
        LocalDate date = date1.plusMonths(Constante.maxFiltersDatesDifference);
        if (date.compareTo(date2) < 0) {
            if (setDate1) {
                date1 = date2.minusMonths(Constante.maxFiltersDatesDifference);
                buttonDate1.setText(String.valueOf(date1));
            } else {
                date2 = date1.plusMonths(Constante.maxFiltersDatesDifference);
                buttonDate2.setText(String.valueOf(date2));
            }
            Toast.makeText(getContext(), "No more than " + Constante.maxFiltersDatesDifference + " month", Toast.LENGTH_SHORT).show();
        }
    }

}
