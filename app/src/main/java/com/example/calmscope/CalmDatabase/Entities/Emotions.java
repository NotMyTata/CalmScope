package com.example.calmscope.CalmDatabase.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emotions")
public class Emotions {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "at_risk")
    private boolean atRisk;

    public Emotions(String type, boolean atRisk) {
        this.type = type;
        this.atRisk = atRisk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAtRisk() {
        return atRisk;
    }

    public void setAtRisk(boolean atRisk) {
        this.atRisk = atRisk;
    }
}
