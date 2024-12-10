package com.example.calmscope.CalmDatabase.Entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "results",
        foreignKeys = {
        @ForeignKey(
                entity = Users.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ),
        @ForeignKey(
                entity = Emotions.class,
                parentColumns = "id",
                childColumns = "emotionId",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
        })
public class Results {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "userId")
    private int userId;
    @ColumnInfo(name = "emotionId")
    private int emotionId;
    @ColumnInfo(name = "date")
    private Date date;

    public Results(int userId, int emotionId, Date date) {
        this.userId = userId;
        this.emotionId = emotionId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
