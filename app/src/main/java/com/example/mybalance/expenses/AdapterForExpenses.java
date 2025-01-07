package com.example.mybalance.expenses;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.example.mybalance.utils.AccountsDatesFotIncAdapter;
import com.example.mybalance.modelsDB.ExpenseType;
import com.example.mybalance.modelsDB.Expenses;

import java.util.List;
import java.util.Map;

public class AdapterForExpenses extends RecyclerView.Adapter<ExpensesViewHolder> {
    List<Expenses> list;
    List<AccountsDatesFotIncAdapter> accountsList;
    Map<Long, ExpenseType> typesMap;
    boolean printAccount = false;
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

        Expenses expenses = list.get(position);
        holder.date.setText(expenses.getDate());
        String amount = "";
        if (accountsList != null && !accountsList.isEmpty()) {
            if (!accountsList.get(position).symbol.isEmpty()) {
                amount = accountsList.get(position).symbol + " ";
            } else {
                amount = accountsList.get(position).charCode + " ";
            }
        }
        amount += String.valueOf(expenses.getAmount());
        holder.amount.setText(amount);
        if (expenses.getName() != null) {
            holder.name.setText(expenses.getName());
        }
        if (printAccount) {
            holder.accountsName.setText("(" + accountsList.get(position).name + ")");
            holder.accountsName.setVisibility(View.VISIBLE);
        } else {
            holder.accountsName.setVisibility(View.GONE);
        }
        if (typesMap !=null) {
            ExpenseType type= typesMap.get(expenses.getExpenseTypeId());
            if (type != null){
                holder.categoryName.setText(type.getName());
            }
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

    public void updateList(List<Expenses> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setTypesMap (Map <Long, ExpenseType> typeMap){
        this.typesMap = typeMap;
    }
}
