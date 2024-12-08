package com.example.calmscope;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    // TODO: solve the problem where the user can back out of disclaimer and still continue to home
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("com.example.calmscope", MODE_PRIVATE);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(prefs.getBoolean("firstrun", true)){
            prefs.edit().putBoolean("firstrun", false).commit();
            startActivity(new Intent(getApplicationContext(), DisclaimerActivity.class));
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}