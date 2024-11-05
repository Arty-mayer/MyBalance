package com.example.mybalance.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    TextView accountsName;
    TextView balance;
    TextView difference;
    TextView incomeTV;
    TextView expenseTV;
    public AccountViewHolder(@NonNull View itemView) {
        super(itemView);
        accountsName = itemView.findViewById(R.id.nameAccTv);
        balance = itemView.findViewById(R.id.balance);
        difference = itemView.findViewById(R.id.difference);
        incomeTV = itemView.findViewById(R.id.incomes);
        expenseTV = itemView.findViewById(R.id.expenses);
    }
}
