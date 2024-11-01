package com.example.mybalance.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybalance.R;

public class ExpensesEditor extends AppCompatActivity {
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_editor);
       // id = 0;
        Intent intentParent = getIntent();
        if (intentParent.hasExtra("expensesId")) {
            id = intentParent.getLongExtra("expensesId", 0);
        } else {
            id = -1;
        }
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_LONG).show();
    }
}
