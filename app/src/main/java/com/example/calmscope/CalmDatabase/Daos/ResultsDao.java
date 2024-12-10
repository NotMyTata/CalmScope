package com.example.calmscope.CalmDatabase.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calmscope.CalmDatabase.Entities.Results;

import java.util.List;

@Dao
public interface ResultsDao {
    @Query("SELECT * FROM Results")
    List<Results> getAll();

    @Insert
    void insertAll(Results... results);

    @Insert
    void insertResult(Results result);

    @Update
    void updateResult(Results result);
}
