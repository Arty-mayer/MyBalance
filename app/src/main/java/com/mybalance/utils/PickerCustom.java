package com.mybalance.utils;

import android.widget.Button;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class PickerCustom   {
    private MaterialDatePicker<Long> picker;
    private OnSelectedListener listener;
    public Button button = null;
    public int dateKey;


    public PickerCustom (){
        picker = MaterialDatePicker.Builder.datePicker().build();

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                if (listener == null){
                    return;
                }
                LocalDate selectedDate = Instant.ofEpochMilli(selection)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                listener.onDateSelected(selectedDate);
            }
        });
    }
    public void show(FragmentManager fragmentManager, String tag){
        picker.show(fragmentManager,tag);
    }

    public void setOnSelectedListener(OnSelectedListener listener){
        this.listener = listener;
    }

    public interface OnSelectedListener{
        void onDateSelected (LocalDate date);
    }
}
