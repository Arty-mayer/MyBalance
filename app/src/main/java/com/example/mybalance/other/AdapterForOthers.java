package com.example.mybalance.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

import java.util.List;

public class AdapterForOthers extends RecyclerView.Adapter<OthersViewHolder> {
    Context context;
    OthersMenuItems list;

    OnItemClickListener listener;

    @NonNull
    @Override
    public OthersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.others_item, parent, false);
        return new OthersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OthersViewHolder holder, int position) {
        holder.name.setText(list.itemsList.get(position).name);
        if (list.itemsList.get(position).classActivity == null){
            holder.name.setEnabled(false);
        }
        holder.setListeners(listener, position);
    }

    @Override
    public int getItemCount() {
        if (list == null || list.itemsList.isEmpty()) {
            return 0;
        }else {
            return list.itemsList.size();
        }
    }

    public void setList(OthersMenuItems list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
