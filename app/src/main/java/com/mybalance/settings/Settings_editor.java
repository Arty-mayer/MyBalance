//TODO переписать LIST на MAP

package com.mybalance.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybalance.R;
import com.mybalance.utils.Constante;
import com.mybalance.data.AppDB;
import com.mybalance.modelsDB.Currency;
import com.mybalance.modelsDB.CurrencyDao;

import java.util.List;
import java.util.concurrent.Executors;

public class Settings_editor extends AppCompatActivity {
    ArrayAdapter<String> currencyListAdapter;
    AutoCompleteTextView charCode;
    Button saveButton;
    Button closeButton;
    TextView currencyTextView;

    List<Currency> currencyList;

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
        currencyTextView = findViewById(R.id.currencyNow);
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
                currencyList = currencyDao.getAllCurrency();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createAdapterForSymbol(currencyList);
                    }
                });
            }
        });
    }

    private void createAdapterForSymbol(List<Currency> currencyList) {
        listOfCurrencyCharCodes = new String[currencyList.size()];
        ids = new int[currencyList.size()];
        selectedCurrencyNum = -1;
        selectedCurrencyCharCode = "";
        for (int i = 0; i < currencyList.size(); i++) {
            listOfCurrencyCharCodes[i] = currencyList.get(i).getChar_code();
            ids[i] = currencyList.get(i).getId();
            if (selectedCurrencyId == currencyList.get(i).getId()) {
                selectedCurrencyNum = i;
                selectedCurrencyCharCode = currencyList.get(i).getChar_code();
            }
        }
        currencyListAdapter.addAll(listOfCurrencyCharCodes);
        currencyListAdapter.notifyDataSetChanged();

        if (selectedCurrencyNum != -1) {
            String currency = selectedCurrencyCharCode+" ("+currencyList.get(selectedCurrencyNum).getName()+")";
            currencyTextView.setText(currency);
        }
    }

    private void setListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charCode.clearFocus();
                //  Toast.makeText(getApplicationContext(),selectedCurrencyCharCode,Toast.LENGTH_SHORT).show();
                if (selectedCurrencyId > 0) {
                    preferences.edit().putInt(Constante.defCurrencyId, selectedCurrencyId).apply();
                    String currency = selectedCurrencyCharCode+" ("+currencyList.get(selectedCurrencyNum).getName()+")";
                    currencyTextView.setText(currency);
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
