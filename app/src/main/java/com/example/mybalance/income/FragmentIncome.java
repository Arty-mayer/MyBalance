package com.example.mybalance.income;

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

import com.example.mybalance.R;
import com.example.mybalance.Utils.AccountsDatesFotIncAdapter;
import com.example.mybalance.Utils.Constante;
import com.example.mybalance.Utils.PickerCustom;
import com.example.mybalance.data.AppDB;
import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.AccountsDao;
import com.example.mybalance.modelsDB.Income;
import com.example.mybalance.modelsDB.IncomeDao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class FragmentIncome extends Fragment {
    Button buttonPlus;
    Button buttonDate1;
    Button buttonDate2;
    Button buttonDate;
    Button buttonAdd;
    EditText editTextAmount;
    EditText editTextName;
    Spinner spinnerAccounts;
    TextView notice1;
    TextView notice2;
    TextView minus;
    RecyclerView incomesView;
    Switch swAllAccounts;

    IncomeViewModel incomeViewModel;
    ArrayAdapter<String> adapterForSpinner = null;
    AdapterForIncome adapterForIncome = null;

    SharedPreferences appPreferences;
    Calendar calendar;
    PickerCustom datePicker;

    // db
    IncomeDao incomeDao = null;
    AccountsDao accountsDao = null;
    List<Accounts> accountsList = null;

    LocalDate dateForNewInc;
    LocalDate date1;
    LocalDate date2;
    int accountId = 0;
    Accounts account;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.income_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findInterfaceItems(view);
        initialisation();
        setButtonPlusSymbol();
        getAccountsFromDb();
        setListeners();
        setObservers();
    }

    private void setObservers() {
        incomeViewModel.getListAccounts().observe(getViewLifecycleOwner(), new Observer<List<Accounts>>() {
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
            }
        });
        incomeViewModel.getListIncome().observe(getViewLifecycleOwner(), new Observer<List<Income>>() {
            @Override
            public void onChanged(List<Income> incomes) {
                adapterForIncome.setAccountsList(makeListAccDatesForIncAdapter(incomes));
                adapterForIncome.updateList(incomes);
                int position = adapterForIncome.getItemCount() - 1;
                if (position > 0) {
                    incomesView.smoothScrollToPosition(adapterForIncome.getItemCount() - 1);
                }
                if (incomesView.getVisibility() == View.GONE){
                    incomesView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private List<AccountsDatesFotIncAdapter> makeListAccDatesForIncAdapter(List<Income> incomes){
        List<AccountsDatesFotIncAdapter> accountsDate = new ArrayList<AccountsDatesFotIncAdapter>();
        for (Income income: incomes) {
            for (Accounts a: incomeViewModel.getListAccounts().getValue()){
                if (a.getId() == income.getAccountsId()){
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
        if (incomeDao == null) {
            incomeDao = db.incomeDao();
        }
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                incomeViewModel.setListAccounts(accountsDao.getAllAccounts());
            }
        });
    }

    private void getIncomesFromDb() {

        AppDB db = AppDB.getDb(getContext());
        if (accountsDao == null) {
            accountsDao = db.accountsDao();
        }
        if (incomeDao == null) {
            incomeDao = db.incomeDao();
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            if (swAllAccounts.isChecked()) {
                adapterForIncome.setPrintAccount(true);
                incomeViewModel.setListIncome(incomeDao.getIncomesRange(date1.toString(), date2.toString()));
            } else if (accountId > 0) {
                adapterForIncome.setPrintAccount(false);
                account = accountsDao.getAccount(accountId);
                incomeViewModel.setListIncome(incomeDao.getIncomesRangeByAccount(date1.toString(), date2.toString(), accountId));
            }
        });
    }

    private void initialisation() {
        incomeViewModel = new IncomeViewModel();
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
        adapterForIncome = new AdapterForIncome();
        incomesView.setLayoutManager(new LinearLayoutManager(getContext()));
        incomesView.setAdapter(adapterForIncome);
        accountId = appPreferences.getInt(Constante.defAccIdName, Constante.defAccIdValue);
    }

    private void findInterfaceItems(View view) {
        buttonPlus = view.findViewById(R.id.plusButton);
        buttonDate1 = view.findViewById(R.id.date1);
        buttonDate2 = view.findViewById(R.id.date2);
        buttonDate = view.findViewById(R.id.date);
        editTextName = view.findViewById(R.id.editTextName);
        buttonAdd = view.findViewById(R.id.addButton);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        spinnerAccounts = view.findViewById(R.id.mySpinner);
        notice1 = view.findViewById(R.id.incomeNotice1);
        notice2 = view.findViewById(R.id.incomeNotice2);
        minus = view.findViewById(R.id.textViewMinus);
        incomesView = view.findViewById(R.id.recyclerViewIncome);
        swAllAccounts = view.findViewById(R.id.swAllAccounts);
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

        final String NAME_1 = "Picker_expense_1";
        buttonDate.setOnClickListener(v -> {
            datePicker.button = (Button) v;
            datePicker.dateKey = 0;
            datePicker.show(getActivity().getSupportFragmentManager(), NAME_1);
        });

        buttonDate1.setOnClickListener(v -> {
            datePicker.button = (Button) v;
            datePicker.dateKey = 1;
            datePicker.show(getActivity().getSupportFragmentManager(), NAME_1);
        });

        buttonDate2.setOnClickListener(v -> {
            datePicker.button = (Button) v;
            datePicker.dateKey = 2;
            datePicker.show(getActivity().getSupportFragmentManager(), NAME_1);
        });

        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    buttonAdd.setEnabled(s.length() > 0);
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
                getIncomesFromDb();
            }
        });

        swAllAccounts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerAccounts.setEnabled(!isChecked);
                getIncomesFromDb();
            }
        });

        spinnerAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                account = incomeViewModel.getListAccounts().getValue().get(position);
                accountId = account.getId();
                getIncomesFromDb();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //the method is not used in the application, but it must be implemented because it is specified in the interface.
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
                Income income = new Income();
                income.setAmount(amount);
                income.setDate(dateForNewInc.toString());
                income.setAccountsId(accountId);
                incomeDao.insert(income);
                float newAmount = account.getAmount() + amount;
                account.setAmount(newAmount);
                accountsDao.update(account);
                incomeViewModel.setListIncome(incomeDao.getIncomesRangeByAccount(date1.toString(), date2.toString(), accountId));
            }
        });
    }

    private void setFilterVisibility(boolean visible) {
        if (visible) {
            notice2.setVisibility(View.VISIBLE);
            minus.setVisibility(View.VISIBLE);
            buttonDate1.setVisibility(View.VISIBLE);
            buttonDate2.setVisibility(View.VISIBLE);
              swAllAccounts.setVisibility(View.VISIBLE);

        } else {
            notice2.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
            buttonDate1.setVisibility(View.GONE);
            buttonDate2.setVisibility(View.GONE);
             swAllAccounts.setVisibility(View.GONE);
        }
    }

    private void setAddFormVisibility(boolean visible) {
        if (visible) {
            buttonDate.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.VISIBLE);
            editTextAmount.setVisibility(View.VISIBLE);
            editTextName.setVisibility(View.VISIBLE);
            if (!spinnerAccounts.isEnabled()) {
                spinnerAccounts.setEnabled(true);
            }
        } else {
            buttonDate.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.GONE);
            editTextAmount.setVisibility(View.GONE);
            editTextName.setVisibility(View.GONE);
            if (swAllAccounts.isChecked()) {
                spinnerAccounts.setEnabled(false);
            }
        }
    }

    private void checkRange(Boolean setDate1) {
        LocalDate date = date1.plusMonths(Constante.maxFiltersDatesDifference);
        if (date.isBefore(date2)) {
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