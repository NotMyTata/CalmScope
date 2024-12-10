package com.example.calmscope.CalmDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calmscope.CalmDatabase.Daos.EmotionsDao;
import com.example.calmscope.CalmDatabase.Daos.ResultsDao;
import com.example.calmscope.CalmDatabase.Daos.UsersDao;
import com.example.calmscope.CalmDatabase.Entities.Emotions;
import com.example.calmscope.CalmDatabase.Entities.Results;
import com.example.calmscope.CalmDatabase.Entities.Users;

@Database(entities = {Users.class, Emotions.class, Results.class}, version = 1)
public abstract class CalmDB extends RoomDatabase {
    public abstract UsersDao usersDao();
    public abstract EmotionsDao emotionsDao();
    public abstract ResultsDao resultsDao();
    private static volatile CalmDB INSTANCE;

    public static CalmDB getInstance(Context context){
        if(INSTANCE == null){
            synchronized (CalmDB.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),CalmDB.class, "CalmDB")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
