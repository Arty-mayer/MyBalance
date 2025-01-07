package com.example.mybalance.accounts;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.example.mybalance.R;

import com.example.mybalance.data.AppDB;
import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.AccountsDao;
import com.example.mybalance.modelsDB.Currency;
import com.example.mybalance.modelsDB.CurrencyDao;

public class AccountsEditor extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText newAccounsEditText;
    private Button addButton;
    private Button plusButton;
    private Button backButton;
    private AccountsViewModel viewModel;
    private AdapterForAccounts adapter;
    private AutoCompleteTextView inputCurrencyCharCode;
    private AutoCompleteTextView inputCurrencyName;
    private final List<TextView> notices = new ArrayList<>();
    private List<Currency> currencies = new ArrayList<>();
    AccountsDao daoAccounts;
    CurrencyDao daoCurrency;
    SoundPool soundPool;
    int soundClickId;

    @Override
    public void onResume() {
        super.onResume();
        loadFromDb();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_activity);

        findInterfaceItems();
        //  createSoundPool(view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recycleViewUpdater();
        editTextHandler();
        addPlusButtonHandler();
        loadFromDb();
        setListeners();
    }
/*
    private void createSoundPool(View view){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundClickId = soundPool.load(getActivity(), R.raw.click, 1);

    }*/

    private void loadFromDb() {
        AppDB db = AppDB.getDb(this);
        daoAccounts = db.accountsDao();
        daoCurrency = db.currencyDao();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Log.d("DB_LOG", "Before fetching accounts");
                currencies = daoCurrency.getAllCurrency();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCurrenciesInput();
                    }
                });
                viewModel.setAccountsList(daoAccounts.getAllAccounts());
                Log.d("DB_LOG", "Accounts fetched: ");
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error fetching accounts", e);
            }
        });
    }

    private void findInterfaceItems() {
        newAccounsEditText = findViewById(R.id.editText_newAcc);
        addButton = findViewById(R.id.addButton);
        plusButton = findViewById(R.id.plusButton);
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerViewAccs);
        inputCurrencyCharCode = findViewById(R.id.symbol);
        inputCurrencyName = findViewById(R.id.autoCompleteTextView2);

        notices.add(findViewById(R.id.notice_1));
        notices.add(findViewById(R.id.notice_2));

    }

    private void recycleViewUpdater() {
        adapter = new AdapterForAccounts();
        recyclerView.setAdapter(adapter);
        viewModel = new AccountsViewModel();
        viewModel.getAccountsList().observe(this, new Observer<List<Accounts>>() {
            @Override
            public void onChanged(List<Accounts> list) {
                adapter.setCurrencies(currencies);
                adapter.updateList(list);
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (!list.isEmpty()) {
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }

            }
        });
    }

    private void editTextHandler() {
        newAccounsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                addButton.setEnabled(s.length() > 2);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCharCode = inputCurrencyCharCode.getText().toString();
                String newName = newAccounsEditText.getText().toString();

                Accounts account = new Accounts();

                Executors.newSingleThreadExecutor().execute(() -> {
                    Currency currency = daoCurrency.getByCharCode(newCharCode);
                    if (currency == null) {
                        runOnUiThread(() -> {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(newAccounsEditText.getWindowToken(), 0);
                        });
                        return;
                    }
                    account.setAmount(0);
                    account.setName(newName);
                    account.setCurrencyId(currency.getId());
                    account.setCurrencyCharCode(currency.getChar_code());
                    account.setCurrencySymbol(currency.getSymbol());
                    daoAccounts.insert(account);
                    runOnUiThread(() -> {
                        newAccounsEditText.setText("");
                        newAccounsEditText.clearFocus();
                        inputCurrencyCharCode.setText("");
                        inputCurrencyCharCode.clearFocus();
                        inputCurrencyName.setText("");
                        inputCurrencyName.clearFocus();

                        loadFromDb();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(newAccounsEditText.getWindowToken(), 0);
                    });
                });
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // soundPool.play(soundClickId,1f,1f,1, 0,1f);
                if (inputCurrencyCharCode.getVisibility() == View.GONE) {
                    newAccounsEditText.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);

                    inputCurrencyCharCode.setVisibility(View.VISIBLE);
                    inputCurrencyName.setVisibility(View.VISIBLE);
                    for (TextView tw : notices) {
                        tw.setVisibility(View.VISIBLE);
                    }
                    plusButton.setText("-");
                } else {
                    newAccounsEditText.setVisibility(View.GONE);
                    addButton.setVisibility(View.GONE);

                    inputCurrencyCharCode.setVisibility(View.GONE);
                    inputCurrencyName.setVisibility(View.GONE);
                    for (TextView tw : notices) {
                        tw.setVisibility(View.GONE);
                    }
                    plusButton.setText("+");
                }
            }
        });
    }

    public void addPlusButtonHandler() {
        if (inputCurrencyCharCode.getVisibility() == View.GONE) {
            plusButton.setText("+");
        } else {
            plusButton.setText("-");
        }
        //plusButton.setSoundEffectsEnabled(false);
    }

    private void setCurrenciesInput() {

        String[] charCode = new String[currencies.size()];
        String[] names = new String[currencies.size()];

        for (int i = 0; i < currencies.size(); i++) {
            charCode[i] = currencies.get(i).getChar_code();
            names[i] = currencies.get(i).getName();
        }
        ArrayAdapter<String> adapterForSymbols = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, charCode);
        ArrayAdapter<String> adapterForNames = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names);

        inputCurrencyCharCode.setAdapter(adapterForSymbols);
        inputCurrencyName.setAdapter(adapterForNames);
        inputCurrencyCharCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputCurrencyCharCode.showDropDown();
            }
        });
        inputCurrencyName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputCurrencyName.showDropDown();
            }
        });

        inputCurrencyCharCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) parent.getItemAtPosition(position);
                int i = 0;
                for (i = 0; i < charCode.length; i++) {
                    if (charCode[i].equals(selectedItem)) {
                        break;
                    }
                }
                inputCurrencyName.setText(names[i]);
                inputCurrencyCharCode.clearFocus();
                newAccounsEditText.requestFocus();
            }
        });
        inputCurrencyName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) parent.getItemAtPosition(position);
                int i = 0;
                for (i = 0; i < names.length; i++) {
                    if (names[i].equals(selectedItem)) {
                        break;
                    }
                }
                inputCurrencyCharCode.setText(charCode[i]);
                inputCurrencyName.clearFocus();
                newAccounsEditText.requestFocus();
            }
        });
    }
}

