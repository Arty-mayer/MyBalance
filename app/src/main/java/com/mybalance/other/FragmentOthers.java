package com.mybalance.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;

public class FragmentOthers extends Fragment {
   RecyclerView rv_others;
   OnItemClickListener onItemClickListener;
    OthersMenuItems menuItems;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.others_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findInterfaceItems(view);
        initialization();
        setListeners();
    }

    private void findInterfaceItems(View view){
       rv_others = view.findViewById(R.id.rvOthers);
    }
    private void initialization (){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_others.setLayoutManager(layoutManager);
        AdapterForOthers adapter = new AdapterForOthers();
        rv_others.setAdapter(adapter);
        menuItems = new OthersMenuItems(getContext());

        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), menuItems.itemsList.get(position).classActivity);
                getContext().startActivity(intent);
            }
        };

        adapter.setListener(onItemClickListener);
        adapter.setList(menuItems);
    }

    private void setListeners(){
        rv_others.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return super.onInterceptTouchEvent(rv, e);
            }
        });
    }


}
