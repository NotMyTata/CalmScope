package com.example.calmscope;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class DisclaimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        MaterialButton btn_ok = findViewById(R.id.disclaimerOK);
        MaterialButton btn_deny = findViewById(R.id.disclaimerNOK);

        btn_ok.setOnClickListener(v -> {
            if(v.getId() == R.id.disclaimerOK){
                // TODO: ask for permission write read access file, camera use, and location
                SharedPreferences prefs = getSharedPreferences("com.example.calmscope", MODE_PRIVATE);
                prefs.edit().putBoolean("firstrun", false).commit();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        btn_deny.setOnClickListener(v -> {
            if(v.getId() == R.id.disclaimerNOK){
                finish();
            }
        });
    }
}