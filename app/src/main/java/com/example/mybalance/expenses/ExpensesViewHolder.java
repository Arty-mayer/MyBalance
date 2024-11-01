package com.example.mybalance.expenses;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.example.mybalance.modelsDB.Expenses;


import java.util.List;

public class ExpensesViewHolder extends RecyclerView.ViewHolder {
    TextView date;
    TextView amount;
    TextView name;
    Button buttonEdit;

    public ExpensesViewHolder(@NonNull View itemView) {
        super(itemView);
        findInterfaceItems(itemView);
    }

    private void findInterfaceItems(View view) {
        date = view.findViewById(R.id.tvDate);
        amount = view.findViewById(R.id.tvAmount);
        name = view.findViewById(R.id.vtName);
        buttonEdit = view.findViewById(R.id.editButton);
    }

    public void setListeners(Context context, List<Expenses> list, int position){
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long expensesId = list.get(position).getId();
                Intent intent = new Intent(context, ExpensesEditor.class);
                intent.putExtra("expensesId", expensesId);
                context.startActivity(intent);
            }
        });
    }
}
