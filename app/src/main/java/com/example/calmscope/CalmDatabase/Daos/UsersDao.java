package com.example.calmscope.CalmDatabase.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calmscope.CalmDatabase.Entities.Users;

import java.util.List;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM Users")
    List<Users> getAll();

    @Query("SELECT * FROM Users WHERE id=:id")
    Users findById(int id);

    @Query("SELECT id FROM Users WHERE username LIKE :username LIMIT 1")
    int getIdByUsername(String username);

    @Query("SELECT * FROM Users WHERE username LIKE :username LIMIT 1")
    Users findByName(String username);

    @Query("SELECT COUNT(*) FROM Users WHERE username LIKE :username LIMIT 1")
    int countByUsername(String username);

    @Insert
    void insertAll(Users... users);

    @Insert
    void insertUser(Users user);

    @Update
    void updateUser(Users user);
}
