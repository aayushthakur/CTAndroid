package com.example.clevertapintegrationsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Identity", "25Nov2025");
        MyApplication.getInstance().getClevertapDefaultInstance().onUserLogin(profileUpdate);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                }
                Log.d("LoginActivity", "onClick() called with:  = [" + new Gson().toJson(intent) + "]");
                startActivity(intent);
                finish();
            }
        });
    }
}