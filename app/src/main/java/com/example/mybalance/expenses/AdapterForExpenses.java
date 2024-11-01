package com.example.mybalance.expenses;

//TODO внести изменения для изменения суммы на счету

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Expenses;

import java.util.List;

public class AdapterForExpenses extends RecyclerView.Adapter<ExpensesViewHolder> {
    List<Expenses> list;
    Accounts account;
    Context context;

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.expenses_item, parent, false);
        return new ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {
        if (account == null || list == null){
            return;
        }
        Expenses expenses = list.get(position);
        holder.date.setText(expenses.getDate());
        String amount = "";
        if (account != null){
            if (account.getCurrencySymbol() != null){
                amount = account.getCurrencySymbol();
            }
            else {
                amount = account.getCurrencyCharCode() + ": ";
            }
        }
        amount += String.valueOf(expenses.getAmount());
        holder.amount.setText(amount);
        if (expenses.getName() != null){
            holder.name.setText(expenses.getName());
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
    public void updateList(List<Expenses> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
