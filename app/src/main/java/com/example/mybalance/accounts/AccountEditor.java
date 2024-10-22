package com.example.mybalance.accounts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybalance.R;
import com.example.mybalance.data.AppDB;
import com.example.mybalance.Utils.Constante;
import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.AccountsDao;
import com.example.mybalance.modelsDB.Currency;
import com.example.mybalance.modelsDB.CurrencyDao;

import java.util.concurrent.Executors;

public class AccountEditor extends AppCompatActivity {

    Button backButton;
    Button saveButton;
    Button deletebutton;
    EditText name;
    EditText description;
    CheckBox defaultCheckBox;
    TextView topic;
    Accounts account;
    Currency currency;
    int id;
    AccountsDao accountsDao;
    CurrencyDao currencyDao;
    SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_editor);
        appPreferences = this.getSharedPreferences(Constante.preferences, this.MODE_PRIVATE);
        findInterfaceItems();
        getIdFromIntent();
        loadDatesFromDb();
        setListeners();
    }

    private void findInterfaceItems() {
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        deletebutton = findViewById(R.id.deleteButton);
        name = findViewById(R.id.editor_name);
        description = findViewById(R.id.editor_description);
        defaultCheckBox = findViewById(R.id.editor_default_cb);
        topic = findViewById(R.id.editor_topic);
        //  notices.add(findViewById(R.id.editor_notice_1));
        // notices.add(findViewById(R.id.editor_notice_2));

    }

    private void loadDatesFromDb() {
        if (accountsDao == null || currencyDao == null) {
            AppDB db = AppDB.getDb(this);
            accountsDao = db.accountsDao();
            currencyDao = db.currencyDao();
        }
        if (id > 0) {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    account = accountsDao.getAccount(id);
                    if (account == null) {
                        return;
                    }
                    currency = currencyDao.getById(account.getCurrencyId());
                    if (currency == null) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setDatesToInterface();
                        }
                    });
                }
            });
        }
    }

    private void getIdFromIntent() {
        Intent intentParent = getIntent();
        if (intentParent.hasExtra("accountId")) {
            id = intentParent.getIntExtra("accountId", 0);
        } else {
            id = -1;
        }
    }

    private void setDatesToInterface() {
        topic.setText(account.getName() + " (" + currency.getChar_code() + ")");
        name.setText(account.getName());
        description.setText(account.getDescription());
        int defaultAccount = appPreferences.getInt("def_acc", 0);
        defaultCheckBox.setChecked(defaultAccount == account.getId());
    }

    private void setListeners() {
        backButton.setOnClickListener(v -> finish());
        deletebutton.setOnClickListener(v -> deleteAccountDialog());
        saveButton.setOnClickListener(v -> updateAccount());
    }

    private void updateAccount() {
        name.clearFocus();
        description.clearFocus();
        account.setName(name.getText().toString());
        account.setDescription(description.getText().toString());
        if (defaultCheckBox.isChecked()){
            appPreferences.edit().putInt("def_acc", account.getId()).apply();
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            accountsDao.updeate(account);
            runOnUiThread(() -> loadDatesFromDb());
        });
    }

    private void deleteAccountDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.confirmation)
                .setMessage(R.string.delete1)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteAccount() {
        Executors.newSingleThreadExecutor().execute(() -> {
            accountsDao.delete(account);
            runOnUiThread(() -> finish());
        });
    }
}
