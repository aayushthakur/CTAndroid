package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        CleverTapAPI cleverTapAPI = MyApplication.getInstance().getClevertapDefaultInstance();

        EditText userPropKey = findViewById(R.id.userPropKey);
        EditText userPropValue = findViewById(R.id.userPropValue);

        findViewById(R.id.profilePush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String k = userPropKey.getText().toString();
               String v= userPropValue.getText().toString();
               if (!TextUtils.isEmpty(k) & !TextUtils.isEmpty(v)) {
                   Map<String, Object> kv = new HashMap<>();
                   kv.put(k,v);
                   cleverTapAPI.pushProfile(kv);
                   Toast.makeText(getApplicationContext(),"pushProfile to : "+cleverTapAPI.getCleverTapID(),Toast.LENGTH_LONG).show();
               }

            }
        });



    }
}