package com.example.calmscope;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calmscope.CalmDatabase.CalmDB;
import com.example.calmscope.CalmDatabase.Daos.UsersDao;
import com.example.calmscope.CalmDatabase.Entities.Users;
import com.google.android.material.button.MaterialButton;

import java.sql.Date;

public class CreateUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        MaterialButton cuBtn = findViewById(R.id.submitCreateUserBtn);
        cuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameET = findViewById(R.id.createUserET);
                String username = usernameET.getText().toString();
                UsersDao usersDao = CalmDB.getInstance(getApplicationContext()).usersDao();

                if(username.isEmpty() || username.length() < 4 || usersDao.countByUsername(username) > 0){
                    Toast.makeText(getApplicationContext(), "Please input a valid username", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "User successfully created", Toast.LENGTH_LONG).show();

                    java.util.Date utilDate = new java.util.Date();
                    java.sql.Date date = new Date(utilDate.getTime());
                    usersDao.insertUser(new Users(username, date));

                    SharedPreferences prefs = getSharedPreferences("com.example.calmscope", MODE_PRIVATE);
                    prefs.edit().putBoolean("firstrun", false).commit();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }
}
