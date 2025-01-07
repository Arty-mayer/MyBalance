package com.mybalance.accounts;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.mybalance.modelsDB.Accounts;

import java.util.List;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView amount;
    Button editButton;

    public AccountViewHolder(@NonNull View itemView) {
        super(itemView);
        amount = itemView.findViewById(R.id.tvAmount);
        name = itemView.findViewById(R.id.tvDate);
        editButton = itemView.findViewById(R.id.editButton);
    }

    public void setListeners(int position, Context context, List<Accounts> accountsList) {
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context,AccountEditor.class);
            intent.putExtra("accountId", accountsList.get(position).getId());
            context.startActivity(intent);
        });
    }
}

