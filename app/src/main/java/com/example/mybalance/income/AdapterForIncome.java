package com.example.mybalance.income;

//TODO внести изменения для изменения суммы на счету
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Income;

import java.util.List;

public class AdapterForIncome extends RecyclerView.Adapter<IncomeViewHolder> {
    List<Income> list;
    Accounts account;
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
        if (account == null || list == null){
            return;
        }
        Income income = list.get(position);
        holder.date.setText(income.getDate());
        String amount = "";
        if (account != null){
            if (account.getCurrencySymbol() != null){
                amount = account.getCurrencySymbol();
            }
            else {
                amount = account.getCurrencyCharCode() + ": ";
            }
        }
        amount += String.valueOf(income.getAmount());
        holder.amount.setText(amount);
        if (income.getName() != null){
            holder.name.setText(income.getName());
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
    public void setAccount(Accounts account){
        this.account = account;
    }
    public void updateList(List<Income> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
