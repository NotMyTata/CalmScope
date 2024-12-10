package com.example.calmscope.CalmDatabase.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Users {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "profile")
    private int profile;
    @ColumnInfo(name = "username")
    private String username;

    public Users(int profile, String username){
        this.profile = profile;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
