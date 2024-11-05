package com.example.mybalance.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.example.mybalance.Utils.Constante;
import com.example.mybalance.income.IncomeViewHolder;

import java.util.List;

public class AccountsAdapterForHomeRV extends RecyclerView.Adapter<AccountViewHolder> {
    List<AccountForRView> list;
    Context context;

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.home_accounts_rv_item, parent, false);
        return new AccountViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountForRView accountForRView = list.get(position);
        holder.accountsName.setText(accountForRView.accountsName);
        String balance = "";
        if (accountForRView.symbol.length()>0){
            balance = accountForRView.symbol;
        }
        else {
            balance = accountForRView.currency;
        }
        String difference = new String(balance) + " " +String.valueOf(accountForRView.difference);
        balance = balance + " " +String.valueOf(accountForRView.Amount);
        holder.balance.setText(balance);
        holder.difference.setText(difference);
        holder.incomeTV.setText(String.valueOf(accountForRView.incomeAmount));
        holder.expenseTV.setText(String.valueOf(accountForRView.expenseAmount));
        if (accountForRView.difference < 0) {
            holder.difference.setTextColor(Color.RED);
        } else {
            holder.difference.setTextColor(Color.GREEN);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void updateList(List<AccountForRView> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }
}
