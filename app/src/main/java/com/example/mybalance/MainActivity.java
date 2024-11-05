package com.example.mybalance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mybalance.Utils.AppSettings;
import com.example.mybalance.Utils.Constante;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.util.concurrent.Executors;

import com.example.mybalance.data.AppDB;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ProgressBar progBar;
    AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultSettings();
        appSettings = new AppSettings(getApplicationContext());

        progBar = findViewById(R.id.progress_bar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setDefaultFocusHighlightEnabled(false);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.nav_accounts) {
                    selectedFragment = new FragmentAccounts();
                }
                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new FragmentHome();
                }
                if (item.getItemId() == R.id.nav_income){
                    selectedFragment = new FragmentIncome();
                }
                if (item.getItemId() == R.id.nav_expenses){
                    selectedFragment = new FragmentExpenses();
                }
                if (item.getItemId() == R.id.nav_others){
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


        setUIEnabled(false);
        progBar.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDB db = AppDB.getDb(this);
            db.getOpenHelper().getWritableDatabase();
            runOnUiThread(() -> {
                progBar.setVisibility(View.GONE);
                setUIEnabled(true);

                // Загрузка первого фрагмента по умолчанию
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentHome())
                        .commit();
            });
        });
    }

    private void setUIEnabled(boolean enabled) {
        bottomNavigationView.setEnabled(enabled);
        if (enabled) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }
    private void setDefaultSettings (){
        SharedPreferences preferences = this.getSharedPreferences(Constante.preferences, this.MODE_PRIVATE);
        if (preferences.contains(Constante.defAccIdName)){
            return;
       }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constante.defAccIdName, Constante.defAccIdValue);
        editor.putInt(Constante.defCurrencyId, Constante.defCurrecyIdValue);
        editor.apply();

    }


}