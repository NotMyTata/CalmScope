package com.example.calmscope.CalmDatabase.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calmscope.CalmDatabase.Entities.Emotions;

import java.util.List;

@Dao
public interface EmotionsDao {
    @Query("SELECT * FROM Emotions")
    List<Emotions> getAll();

    @Insert
    void insertAll(Emotions... emotions);

    @Insert
    void insertEmotion(Emotions emotion);

    @Update
    void updateEmotion(Emotions emotion);
}
