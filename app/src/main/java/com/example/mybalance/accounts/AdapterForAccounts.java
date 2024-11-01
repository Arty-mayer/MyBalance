package com.example.mybalance.accounts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

import java.util.List;

import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Currency;

public class AdapterForAccounts extends RecyclerView.Adapter<AdapterForAccounts.AccountViewHolder> {
    private List<Accounts> list;
    private List<Currency> currencies;
    Context context;
    public AdapterForAccounts() {
        list = null;
        currencies = null;
        // this.listener = listener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_item, parent, false);
        AccountViewHolder viewHolder = new AccountViewHolder(view);
        return viewHolder;
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
        String str = context.getString(R.string.balance)+": ";
        if (currency != null && currency.getSymbol().length() > 0) {
            str = str + currency.getSymbol()+" ";
        }
        else {
            str += currency.getChar_code() + " ";
        }
        str = str + String.valueOf(account.getAmount());
        holder.name.setText(account.getName() + " (" + currency.getChar_code() + ")");
        holder.amount.setText(str);
        if (account.getAmount() >= 0) {
            TypedValue tp = new TypedValue();

            holder.amount.setTextColor(context.getApplicationContext().getColor(R.color.green1));

        } else if (account.getAmount() < 0) {
            holder.amount.setTextColor(Color.RED);
        }
        holder.setListeners(position, context);
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

    class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;
        Button editButton;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.tvAmount);
            name = itemView.findViewById(R.id.tvDate);
            editButton = itemView.findViewById(R.id.editButton);
        }

        public void setListeners(int position, Context context) {
            editButton.setOnClickListener(v -> {
                int accountId = list.get(position).getId();
                Intent intent = new Intent(context, AccountEditor.class);
                intent.putExtra("accountId", list.get(position).getId());
                context.startActivity(intent);
            });
        }
    }
}
