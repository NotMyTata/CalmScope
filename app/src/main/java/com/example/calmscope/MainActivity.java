package com.example.calmscope;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.calmscope.CalmDatabase.CalmDB;
import com.example.calmscope.CalmDatabase.Daos.EmotionsDao;
import com.example.calmscope.CalmDatabase.Daos.ResultsDao;
import com.example.calmscope.CalmDatabase.Entities.Emotions;
import com.example.calmscope.CalmDatabase.Entities.Results;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDatabase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    private void initializeDatabase(){
        CalmDB database = CalmDB.getInstance(getApplicationContext());
        EmotionsDao emotionsDao = database.emotionsDao();
//        ResultsDao resultsDao = database.resultsDao();

        Emotions[] emotion = new Emotions[]{
                new Emotions("Sad", true),
                new Emotions("Calm", false)
        };
        for (Emotions e : emotion) {
            if(emotionsDao.countByType(e.getType()) == 0){
                emotionsDao.insertEmotion(e);
            }
        }

        // Mock result
//        resultsDao.reset();
//
//        java.util.Date utilDate = new java.util.Date();
//        java.sql.Date date = new java.sql.Date(utilDate.getTime());
//
//        Results[] results = new Results[]{
//                new Results(1,1,date),
//                new Results(1,2,date),
//                new Results(1,1,date),
//                new Results(1,1,date),
//                new Results(1,2,date)
//        };
//        for (Results result : results){
//            resultsDao.insertResult(result);
//        }
    }
}