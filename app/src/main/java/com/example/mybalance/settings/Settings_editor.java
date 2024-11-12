package com.example.mybalance.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybalance.R;
import com.example.mybalance.Utils.Constante;
import com.example.mybalance.data.AppDB;
import com.example.mybalance.modelsDB.Currency;
import com.example.mybalance.modelsDB.CurrencyDao;

import java.util.List;
import java.util.concurrent.Executors;

public class Settings_editor extends AppCompatActivity {
    ArrayAdapter<String> currencyListAdapter;
    AutoCompleteTextView charCode;
    Button saveButton;
    Button closeButton;


    int[] ids;
    String[] listOfCurrencyCharCodes;
    String selectedCurrencyCharCode;
    int selectedCurrencyId;
    int selectedCurrencyNum;

    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        findInterfaceItems();
        initialization();
        loadCurrencyFromDb();

        setListeners();
    }


    private void findInterfaceItems() {
        charCode = findViewById(R.id.symbol);
        saveButton = findViewById(R.id.saveButton);
        closeButton = findViewById(R.id.closeButton);
    }

    private void initialization() {
        currencyListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        charCode.setAdapter(currencyListAdapter);
        preferences = this.getSharedPreferences(Constante.preferences, Context.MODE_PRIVATE);
        selectedCurrencyId = preferences.getInt(Constante.defCurrencyId, Constante.defCurrecyIdValue);
        selectedCurrencyNum = -1;
    }

    private void loadCurrencyFromDb() {
        AppDB db = AppDB.getDb(this);
        CurrencyDao currencyDao = db.currencyDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<Currency> currency = currencyDao.getAllCurrency();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createAdapterForSymbol(currency);
                    }
                });
            }
        });
    }

    private void createAdapterForSymbol(List<Currency> accountsList) {
        listOfCurrencyCharCodes = new String[accountsList.size()];
        ids = new int[accountsList.size()];
        for (int i = 0; i < accountsList.size(); i++) {
            listOfCurrencyCharCodes[i] = accountsList.get(i).getChar_code();
            ids[i] = accountsList.get(i).getId();
            if (selectedCurrencyId == accountsList.get(i).getId()) {
                selectedCurrencyNum = i;
                selectedCurrencyCharCode = accountsList.get(i).getChar_code();
            }
        }
        currencyListAdapter.addAll(listOfCurrencyCharCodes);
        currencyListAdapter.notifyDataSetChanged();
        charCode.setText(selectedCurrencyCharCode);
    }

    private void setListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charCode.clearFocus();
                //  Toast.makeText(getApplicationContext(),selectedCurrencyCharCode,Toast.LENGTH_SHORT).show();
                if (selectedCurrencyId > 0) {
                    preferences.edit().putInt(Constante.defCurrencyId, selectedCurrencyId).apply();
                }
            }
        });

        charCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrencyNum = position;
                selectedCurrencyCharCode = listOfCurrencyCharCodes[position];
            }
        });

        charCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    charCode.showDropDown();
                } else {
                    selectedCurrencyNum = -1;
                    String input = charCode.getText().toString();
                    for (int i = 0; i < listOfCurrencyCharCodes.length; i++) {
                        if (listOfCurrencyCharCodes[i].equalsIgnoreCase(input)) {
                            selectedCurrencyNum = i;
                            selectedCurrencyCharCode = listOfCurrencyCharCodes[i];
                            selectedCurrencyId = ids[i];
                            charCode.setText(selectedCurrencyCharCode);
                            break;
                        }
                    }
                    if (selectedCurrencyNum == -1) {
                        charCode.setText("");
                    }
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
