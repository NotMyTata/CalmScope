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

    @Query("SELECT * FROM Emotions WHERE id=:id")
    Emotions findById(int id);

    @Query("SELECT * FROM Emotions WHERE type LIKE :type LIMIT 1")
    Emotions findByType(String type);

    @Insert
    void insertAll(Emotions... emotions);

    @Insert
    void insertEmotion(Emotions emotion);

    @Update
    void updateEmotion(Emotions emotion);
}
