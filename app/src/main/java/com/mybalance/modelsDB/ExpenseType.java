package com.mybalance.modelsDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index(value = "sortOrder")})
public class ExpenseType implements InterfaceForTypes {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;

    public long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(long sortOrder) {
        this.sortOrder = sortOrder;
    }

    private long sortOrder;

    @Ignore
    public ExpenseType (String name){
        this.name = name;
    }

    public ExpenseType(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @NonNull
    public String toString (){
        return (name != null)? name : "";
    }
}

