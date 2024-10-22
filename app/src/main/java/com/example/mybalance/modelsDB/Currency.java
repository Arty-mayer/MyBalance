package com.example.mybalance.modelsDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Currency {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int code;
    public String char_code;
    public String symbol;

    public String name;
    public String name_de;
    public String name_ru;

    public Currency(int code, String char_code, String symbol, String name, String name_de, String name_ru) {
        this.code = code;
        this.char_code = char_code;
        this.symbol = symbol;
        this.name = name;
        this.name_de = name_de;
        this.name_ru = name_ru;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getChar_code() {
        return char_code;
    }

    public void setChar_code(String char_code) {
        this.char_code = char_code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_de() {
        return name_de;
    }

    public void setName_de(String name_de) {
        this.name_de = name_de;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }
}
