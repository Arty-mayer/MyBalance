package com.example.mybalance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import java.time.Instant;

public class FragmentExpenses extends Fragment {
    public View onCreate(LayoutInflater inflater, ViewGroup container, Bundle saveInstandsState){
        return inflater.inflate(R.layout.fragment_expenses, container,false);
    }
}
