package com.mybalance.income;

import android.content.Context;
import androidx.appcompat.widget.SwitchCompat;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.mybalance.utils.AccountsDatesFotIncAdapter;
import com.mybalance.utils.Constante;
import com.mybalance.utils.PickerCustom;
import com.mybalance.data.AppDB;
import com.mybalance.modelsDB.Accounts;
import com.mybalance.modelsDB.AccountsDao;
import com.mybalance.modelsDB.Income;
import com.mybalance.modelsDB.IncomeDao;
import com.mybalance.modelsDB.IncomeType;
import com.mybalance.modelsDB.IncomeTypeDao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    SwitchCompat swAllAccounts;
    AutoCompleteTextView incomeType;

    IncomeViewModel incomeViewModel;
    ArrayAdapter<String> adapterForSpinner = null;
    AdapterForIncome adapterForIncome = null;

    SharedPreferences appPreferences;
    Calendar calendar;
    PickerCustom datePicker;

    // db
    IncomeDao incomeDao = null;
    AccountsDao accountsDao = null;
    IncomeTypeDao incomeTypeDao = null;

    LocalDate dateForNewInc;
    LocalDate date1;
    LocalDate date2;
    int accountId = 0;
    Accounts account;
    List<IncomeType> incomeTypeList;

    ArrayAdapter<String> adapterForIncomeTypes;
    Map<Long, IncomeType> mapIncomeType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.income_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findInterfaceItems(view);
        initialisation();
        setButtonPlusSymbol();
        setListeners();
        setObservers();
        getAccAndIncomeTypesFromDb();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAccAndIncomeTypesFromDb();
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

                List<String> itemsForTypes = new ArrayList<>();
                for (IncomeType type : incomeTypeList) {
                    itemsForTypes.add(type.getName());
                    mapIncomeType.put(type.getId(), type);
                }
                adapterForIncomeTypes = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, itemsForTypes);
                adapterForIncomeTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                incomeType.setAdapter(adapterForIncomeTypes);
                adapterForIncome.setTypesMap(mapIncomeType);
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
                if (incomesView.getVisibility() == View.GONE) {
                    incomesView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private List<AccountsDatesFotIncAdapter> makeListAccDatesForIncAdapter(List<Income> incomes) {
        List<AccountsDatesFotIncAdapter> accountsDate = new ArrayList<>();
        for (Income income : incomes) {
            List<Accounts> accountsList = incomeViewModel.getListAccounts().getValue();
            if (accountsList == null) {
                continue;
            }
            for (Accounts a : accountsList) {
                if (a.getId() == income.getAccountsId()) {
                    accountsDate.add(new AccountsDatesFotIncAdapter(a.getName(), a.getCurrencySymbol(), a.getCurrencyCharCode()));
                    break;
                }
            }
        }
        return accountsDate;
    }

    private void getAccAndIncomeTypesFromDb() {
        getDaos();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                incomeTypeList = incomeTypeDao.getAllIncomeTypes();
                incomeViewModel.setListAccounts(accountsDao.getAllAccounts());
            }
        });
    }

    private void getDaos() {
        AppDB db = AppDB.getDb(getContext());
        if (accountsDao == null) {
            accountsDao = db.accountsDao();
            incomeTypeDao = db.incomeTypeDao();
            incomeDao = db.incomeDao();
        }
    }

    private void getIncomesFromDb() {

        getDaos();

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
        appPreferences = this.getActivity().getSharedPreferences(Constante.preferences, Context.MODE_PRIVATE);
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

        mapIncomeType = new HashMap<>();
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
        incomeType = view.findViewById(R.id.incomeType);
    }

    private void setButtonPlusSymbol() {
        if (editTextAmount.getVisibility() == View.GONE) {
            buttonPlus.setText(getText(R.string.add));
        } else {
            buttonPlus.setText(getText(R.string.to_filter));
        }
    }

    private void setListeners() {
        incomeType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    incomeType.showDropDown();
                }
            }
        });
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
                //the method is not used in the application, but it must be implemented because it is specified in the interface.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonAdd.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //the method is not used in the application, but it must be implemented because it is specified in the interface.
            }
        });

        buttonAdd.setOnClickListener(v -> {
            addInDb();
            editTextAmount.setText("");
            editTextAmount.clearFocus();
            Context context = getContext();
            if (context != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
            }
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
                    default:
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
                List<Accounts> accounts = incomeViewModel.getListAccounts().getValue();
                if (accounts != null) {
                    account = incomeViewModel.getListAccounts().getValue().get(position);
                    accountId = account.getId();
                    getIncomesFromDb();
                }
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

                int incTypePos = adapterForIncomeTypes.getPosition(incomeType.getText().toString());
                long incTypeId = (incTypePos < 1) ? 1 : incomeTypeList.get(incTypePos).getId();
                Income income = new Income();
                if (!editTextName.getText().toString().isEmpty()) {
                    income.setName(editTextName.getText().toString());
                }
                income.setAmount(amount);
                income.setIncomeTypeId(incTypeId);
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
            incomeType.setVisibility(View.VISIBLE);
            if (!spinnerAccounts.isEnabled()) {
                spinnerAccounts.setEnabled(true);
            }
        } else {
            buttonDate.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.GONE);
            editTextAmount.setVisibility(View.GONE);
            editTextName.setVisibility(View.GONE);
            incomeType.setVisibility(View.GONE);
            if (swAllAccounts.isChecked()) {
                spinnerAccounts.setEnabled(false);
            }
        }
    }

    private void checkRange(boolean setDate1) {
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