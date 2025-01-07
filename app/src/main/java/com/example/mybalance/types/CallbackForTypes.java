package com.example.mybalance.types;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CallbackForTypes extends ItemTouchHelper.Callback {
    SwipeHandler swipeHandler;
    MoveHandler moveHandler;

    public CallbackForTypes() {

    }
    public CallbackForTypes(SwipeHandler swipeHandler, MoveHandler moveHandler) {
        this.swipeHandler = swipeHandler;
        this.moveHandler = moveHandler;
    }

    public void setSwipeHandler(SwipeHandler handler) {
        swipeHandler = handler;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        // Сообщаем адаптеру о перемещении
        AdapterForRVTypes adapter = (AdapterForRVTypes) recyclerView.getAdapter();
        if (adapter != null) {
            Log.i("TAG", "onMove: from " + String.valueOf(fromPosition) + " to " +
                    String.valueOf(toPosition));
            adapter.moveItem(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Log.d("RecyclerView", "clearView called for position: " + viewHolder.getAdapterPosition());
        viewHolder.itemView.setAlpha(1.0f);
        moveHandler.onMoveEnd(recyclerView.getAdapter());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (isCurrentlyActive) {

                viewHolder.itemView.setAlpha(0.5f);
            } else {

                viewHolder.itemView.setAlpha(1.0f);
            }
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
       swipeHandler.onSwipe(viewHolder, direction);
    }
};

