package com.example.mybalance.income;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

import com.example.mybalance.Utils.AccountsDatesFotIncAdapter;
import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Income;

import java.util.List;

public class AdapterForIncome extends RecyclerView.Adapter<IncomeViewHolder> {
    List<Income> list;
    List<AccountsDatesFotIncAdapter> accountsList;
    boolean printAccount = false;
    Context context;

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.income_item, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {

        Income income = list.get(position);
        holder.date.setText(income.getDate());
        String amount = "";
        if (accountsList != null && !accountsList.isEmpty()) {
            if (!accountsList.get(position).symbol.isEmpty()) {
                amount = accountsList.get(position).symbol + " ";
            } else {
                amount = accountsList.get(position).charCode + " ";
            }
        }
        amount += String.valueOf(income.getAmount());
        holder.amount.setText(amount);
        if (income.getName() != null) {
            holder.name.setText(income.getName());
        }
        if (printAccount) {
            holder.accountsName.setText("(" + accountsList.get(position).name + ")");
            holder.accountsName.setVisibility(View.VISIBLE);
        } else {
            holder.accountsName.setVisibility(View.GONE);

        }
        holder.setListeners(context, list, position);
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void setPrintAccount(boolean print) {
        printAccount = print;
    }

    public void setAccountsList(List<AccountsDatesFotIncAdapter> list) {
        accountsList = list;
    }

    public void updateList(List<Income> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
