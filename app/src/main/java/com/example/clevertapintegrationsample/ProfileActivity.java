package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        CleverTapAPI cleverTapAPI = MyApplication.getInstance().getClevertapDefaultInstance();

        EditText profileName = findViewById(R.id.profileName);
        EditText profilePhone = findViewById(R.id.profilePhone);
        EditText profileIdentity = findViewById(R.id.profileIdentity);
        Button profileDatePicker = findViewById(R.id.profileDatePicker);
        TextView selectedDateTV = findViewById(R.id.selectedDateTV);
        EditText userPropKey = findViewById(R.id.userPropKey);
        EditText userPropValue = findViewById(R.id.userPropValue);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//          SharedPreferences preferences = getSharedPreferences("WizRocket_ARP",MODE_PRIVATE);


        findViewById(R.id.profilePush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String name = profileName.getText().toString();
//                String phone = profileName.getText().toString();
                String identity = profileIdentity.getText().toString();
//                String selectedDate = profileName.getText().toString();

                /*String k = userPropKey.getText().toString();
               String v= userPropValue.getText().toString();
               if (!TextUtils.isEmpty(k) & !TextUtils.isEmpty(v)) {
                   Map<String, Object> kv = new HashMap<>();
                   kv.put(k,v);
                   cleverTapAPI.pushProfile(kv);
                   Toast.makeText(getApplicationContext(),"pushProfile to : "+cleverTapAPI.getCleverTapID(),Toast.LENGTH_LONG).show();
               }*/
                Map<String, Object> kv = new HashMap<>();
                kv.put("Identity",identity);
                cleverTapAPI.pushProfile(kv);


            }
        });


    }
}