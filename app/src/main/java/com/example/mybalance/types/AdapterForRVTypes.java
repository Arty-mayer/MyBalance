package com.example.mybalance.types;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.example.mybalance.modelsDB.IncomeType;
import com.example.mybalance.modelsDB.InterfaceForTypes;

import java.util.List;
import java.util.zip.Inflater;

public class AdapterForRVTypes extends RecyclerView.Adapter<ViewHolderForTypes> {
    List<? extends InterfaceForTypes> list;
    Context context;
    int[] movePositions = new int[2];
    EditHandler editHandler;
    public final int forType;


    public AdapterForRVTypes(int forType, EditHandler handler) {
        this.forType = forType;
        editHandler = handler;
        movePositions[0] = -1;
        movePositions[1] = -1;
    }

    @NonNull
    @Override
    public ViewHolderForTypes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.types_item, parent, false);
        return new ViewHolderForTypes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForTypes holder, int position) {
        holder.itemView.setAlpha(1.0f);
        holder.nameTextView.setText(list.get(position).getName());
        holder.setEditHandler(editHandler);
        holder.setListeners(position, forType);
    }

    @Override
    public int getItemCount() {
        if (list == null || list.isEmpty()) {
            return 0;
        } else return list.size();
    }

    public void setList(List<? extends InterfaceForTypes> list) {
        this.list = list;
    }

    public void addItem(InterfaceForTypes item) {
        List<InterfaceForTypes> mutableList = (List<InterfaceForTypes>) list;
        mutableList.add(item);
        notifyItemInserted(mutableList.size() - 1);
    }

    public void moveItem(int fromPosition, int toPosition) {
        InterfaceForTypes item = list.remove(fromPosition);
        List<InterfaceForTypes> mutableList = (List<InterfaceForTypes>) list;
        mutableList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        movePositions[1] = toPosition;
        if (movePositions[0]<0){
            movePositions[0] = fromPosition;
        }
    }

    public InterfaceForTypes getType(int position) {
        return list.get(position);
    }

    public void deleteItem(int position) {
        long sortOrder = list.get(position).getSortOrder();
        list.remove(position);
        for (InterfaceForTypes l : list) {
            long so = l.getSortOrder();
            if (so > sortOrder) {
                l.setSortOrder(so);
            }
        }
        notifyItemRemoved(position);
    }

    public int[] getMovePositions() {
        int[] mp = new int[2];
        mp[0] = movePositions[0];
        mp[1] = movePositions[1];
        movePositions[0] = -1;
        movePositions[1] = -1;
        return mp;
    }

    public void organizeSortOrdersInList() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSortOrder((long) i);
        }
    }

}
