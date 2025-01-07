package com.example.mybalance.types;

import android.media.effect.Effect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;


public class ViewHolderForTypes extends RecyclerView.ViewHolder {
    Button editButton;
    TextView nameTextView;
    EditText nameEdittext;

    boolean isOnEdition;
    EditHandler editHandler;
    public ViewHolderForTypes(@NonNull View itemView) {
        super(itemView);
        isOnEdition = false;
        editButton = itemView.findViewById(R.id.editButton);
        nameEdittext = itemView.findViewById(R.id.editTextTypeName);
        nameTextView = itemView.findViewById(R.id.textViewName);
    }

    public void endsOfEdition(){
        isOnEdition = false;
        nameEdittext.setText(nameTextView.getText());
        switchVisibility();
    }
    public void setListeners(int position, int forType){
        this.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                endsOfEdition();
            }
        });

    nameEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if (!hasFocus){
                endsOfEdition();
               }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnEdition){
                    isOnEdition = false;
                    nameTextView.setText(nameEdittext.getText());
                    switchVisibility();
                    if (editHandler != null){
                        editHandler.onSave(position, nameEdittext.getText().toString(), forType);
                    }
                }
                else{
                    isOnEdition = true;
                    nameEdittext.setText(nameTextView.getText());
                    switchVisibility();
                }
            }
        });
    }
    public void setEditHandler(EditHandler handler){
        editHandler = handler;
    }
    private void switchVisibility (){
        if (isOnEdition){

            nameTextView.setVisibility(View.INVISIBLE);
            nameEdittext.setVisibility(View.VISIBLE);
            nameEdittext.requestFocus();
            editButton.setText("Save");
        }
        else {

            nameEdittext.setVisibility(View.INVISIBLE);
            nameTextView.setVisibility(View.VISIBLE);
            editButton.setText("Edit");
        }
    }
}
