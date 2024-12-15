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

    @Query("SELECT * FROM Results ORDER BY date DESC")
    List<Results> getAllDesc();

    @Query("SELECT COUNT(*) FROM Results")
    int getTotal();

    @Query("SELECT COUNT(*) FROM Results JOIN Emotions ON (Results.emotionId=Emotions.id) WHERE Emotions.at_risk = 1")
    int getTotalRisk();

    @Query("SELECT * FROM Results WHERE userId=:userId")
    List<Results> getAllByUserId(int userId);

    @Query("SELECT * FROM Results ORDER BY date DESC LIMIT 3")
    List<Results> fetchLastThree();

    @Query("SELECT COUNT(*) FROM Results WHERE date >= :startDate AND date <= :endDate")
    int countFromDate(Date startDate, Date endDate);

    @Query("SELECT COUNT(*) FROM Results JOIN Emotions ON (Results.emotionId=Emotions.id) WHERE Emotions.at_risk = 1 AND date >= :startDate AND date <= :endDate")
    int countRiskFromDate(Date startDate, Date endDate);

    @Query("DELETE FROM Results")
    void reset();

    @Insert
    void insertAll(Results... results);

    @Insert
    void insertResult(Results result);

    @Delete
    void deleteResult(Results result);

    @Update
    void updateResult(Results result);
}
