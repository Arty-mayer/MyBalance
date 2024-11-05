package com.example.mybalance.income;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.example.mybalance.modelsDB.Income;


import java.util.List;

public class IncomeViewHolder extends RecyclerView.ViewHolder {
    TextView date;
    TextView amount;
    TextView name;
    TextView accountsName;
    Button buttonEdit;

    public IncomeViewHolder(@NonNull View itemView) {
        super(itemView);
        findInterfaceItems(itemView);
    }

    private void findInterfaceItems(View view) {
        date = view.findViewById(R.id.tvDate);
        amount = view.findViewById(R.id.tvAmount);
        name = view.findViewById(R.id.vtName);
        buttonEdit = view.findViewById(R.id.editButton);
        accountsName = view.findViewById(R.id.accountsName);
    }

    public void setListeners(Context context, List<Income> list, int position){
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long incomesId = list.get(position).getId();
                Intent intent = new Intent(context, IncomeEditor.class);
                intent.putExtra("incomeId", incomesId);
                context.startActivity(intent);
            }
        });
    }
}
