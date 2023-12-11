package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        CleverTapAPI cleverTapAPI = MyApplication.getInstance().getClevertapDefaultInstance();

        EditText eventName = findViewById(R.id.eventName);
        EditText eventPropKey = findViewById(R.id.eventPropKey);
        EditText eventPropValue = findViewById(R.id.eventPropValue);

        findViewById(R.id.eventPush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eventName.getText().toString().toLowerCase();

                String k = eventPropKey.getText().toString().toLowerCase();
                String v= eventPropValue.getText().toString().toLowerCase();

                Map<String, Object> kv = new HashMap<>();
                if (!TextUtils.isEmpty(k) & !TextUtils.isEmpty(v)) {
                    kv.put(k,v);
                }
                Toast.makeText(getApplicationContext(),"event push to : "+cleverTapAPI.getCleverTapID(),Toast.LENGTH_LONG).show();
                cleverTapAPI.pushEvent(name,kv);
            }
        });


    }
}