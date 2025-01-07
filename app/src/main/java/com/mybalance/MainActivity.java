package com.mybalance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mybalance.R;
import com.mybalance.utils.AppSettings;
import com.mybalance.utils.Constante;
import com.mybalance.accounts.AccountsEditor;
import com.mybalance.expenses.FragmentExpenses;
import com.mybalance.home.FragmentHome;
import com.mybalance.income.FragmentIncome;
import com.mybalance.other.FragmentOthers;
import com.mybalance.settings.Settings_editor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.util.concurrent.Executors;

import com.mybalance.data.AppDB;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ProgressBar progBar;
    AppSettings appSettings;
    SharedPreferences preferences;
    boolean firstLaunch = true;
    boolean acountsActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = this.getSharedPreferences(Constante.preferences, this.MODE_PRIVATE);
        if (!preferences.contains(Constante.defCurrencyId)) {
            firstLaunch = true;
            Intent intent = new Intent(this, Settings_editor.class);
            startActivity(intent);
        }else {
            firstLaunch = false;
            setDefaultSettings();
            appSettings = new AppSettings(getApplicationContext());
            findInterfaces();
            setListeners();
            openDB();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstLaunch && acountsActivity){
            firstLaunch = false;
            Intent intent = new Intent(this, AccountsEditor.class);
            startActivity(intent);
        }
        if (firstLaunch){
            acountsActivity = true;

            setDefaultSettings();
            appSettings = new AppSettings(getApplicationContext());
            findInterfaces();
            setListeners();
            openDB();

        }
    }

    private void openDB() {
        //setUIEnabled(false);
        progBar.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDB db = AppDB.getDb(this);
            db.getOpenHelper().getWritableDatabase();

            runOnUiThread(() -> {
                progBar.setVisibility(View.GONE);
                //setUIEnabled(true);

                // Загрузка первого фрагмента по умолчанию
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentHome())
                        .commit();
            });
        });
    }

    public void setListeners() {

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
//                if (item.getItemId() == R.id.nav_reports) {
//                    selectedFragment = new FragmentReports();
//                }
                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new FragmentHome();
                }
                if (item.getItemId() == R.id.nav_income) {
                    selectedFragment = new FragmentIncome();
                }
                if (item.getItemId() == R.id.nav_expenses) {
                    selectedFragment = new FragmentExpenses();
                }
                if (item.getItemId() == R.id.nav_others) {
                    selectedFragment = new FragmentOthers();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }

                return false;
            }
        });
    }

    private void findInterfaces() {
        progBar = findViewById(R.id.progress_bar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setDefaultFocusHighlightEnabled(false);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void setUIEnabled(boolean enabled) {
        bottomNavigationView.setEnabled(enabled);
        if (enabled) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    private void setDefaultSettings() {

        if (preferences.contains(Constante.defAccIdName)) {
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constante.defAccIdName, Constante.defAccIdValue);
       // editor.putInt(Constante.defCurrencyId, Constante.defCurrecyIdValue);
        editor.apply();

    }


}