package com.mybalance.accounts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

import java.util.List;

import com.mybalance.modelsDB.Accounts;
import com.mybalance.modelsDB.Currency;

public class AdapterForAccounts extends RecyclerView.Adapter<AccountViewHolder> {
    private List<Accounts> list;
    private List<Currency> currencies;
    Context context;

    public AdapterForAccounts() {
        list = null;
        currencies = null;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_item, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Accounts account = list.get(position);
        Currency currency = null;
        if (account.getCurrencyId() > 0) {
            for (Currency c : currencies) {
                if (c.getId() == account.getCurrencyId()) {
                    currency = c;
                    break;
                }
            }
        }
        String str = context.getString(R.string.balance) + ": ";
        String charCode = "";
        if (currency != null) {
            if (!currency.getChar_code().isEmpty()) {
                charCode = currency.getChar_code();
            }
            if (!currency.getSymbol().isEmpty()) {
                str = str + currency.getSymbol() + " ";
            } else {
                str += charCode + " ";
            }
        }
        str = str + account.getAmount();
        holder.name.setText(account.getName() + " (" + charCode + ")");
        holder.amount.setText(str);
        if (account.getAmount() >= 0) {
            holder.amount.setTextColor(context.getApplicationContext().getColor(R.color.green1));
        } else if (account.getAmount() < 0) {
            holder.amount.setTextColor(Color.RED);
        }
        holder.setListeners(position, context, list);
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void updateList(List<Accounts> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public void setCurrencies(List<Currency> list) {
        this.currencies = list;
    }


}
