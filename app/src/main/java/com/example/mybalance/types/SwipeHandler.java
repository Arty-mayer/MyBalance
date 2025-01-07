package com.example.mybalance.types;

import androidx.recyclerview.widget.RecyclerView;

public interface SwipeHandler {
    void onSwipe (RecyclerView.ViewHolder viewHolder, int direction);
}
