package com.mybalance.other;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

class OthersViewHolder extends RecyclerView.ViewHolder {
    TextView name;
  //  OnItemClickListener listener;

    public OthersViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);

    }

    public void setListeners( OnItemClickListener listener, int position){

        if (listener == null){
            return;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }

}