package com.example.mybalance.income;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybalance.R;

public class IncomeEditor extends AppCompatActivity {
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_editor);
       // id = 0;
        Intent intentParent = getIntent();
        if (intentParent.hasExtra("incomeId")) {
            id = intentParent.getLongExtra("incomeId", 0);
        } else {
            id = -1;
        }
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_LONG).show();
    }
}
