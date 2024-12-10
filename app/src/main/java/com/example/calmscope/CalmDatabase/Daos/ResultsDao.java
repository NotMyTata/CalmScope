package com.example.calmscope.CalmDatabase.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calmscope.CalmDatabase.Entities.Results;

import java.sql.Date;
import java.util.List;

@Dao
public interface ResultsDao {
    @Query("SELECT * FROM Results")
    List<Results> getAll();

    @Query("SELECT COUNT(*) FROM Results")
    int getTotal();

    @Query("SELECT * FROM Results WHERE userId=:userId")
    List<Results> getAllByUserId(int userId);

    @Insert
    void insertAll(Results... results);

    @Insert
    void insertResult(Results result);

    @Delete
    void deleteResult(Results result);

    @Update
    void updateResult(Results result);
}
