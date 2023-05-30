package com.example.clevertapintegrationsample;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.CleverTapAPI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//          SharedPreferences preferences = getSharedPreferences("WizRocket_ARP",MODE_PRIVATE);

        profileDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                selectedDateTV.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        findViewById(R.id.profilePush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = profileName.getText().toString();
                String phone = profilePhone.getText().toString();
                String identity = profileIdentity.getText().toString();
                String selectedDate = selectedDateTV.getText().toString();

                String k = userPropKey.getText().toString();
                String v= userPropValue.getText().toString();

                Map<String, Object> kv = new HashMap<>();
                kv.put("Identity", identity);
                kv.put("Name", name);
                kv.put("Phone", phone);
                DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = sourceFormat.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                kv.put("DOB", date);

                if (!TextUtils.isEmpty(k) & !TextUtils.isEmpty(v)) {
                   kv.put(k,v);
               }
                Toast.makeText(getApplicationContext(),"pushProfile to : "+cleverTapAPI.getCleverTapID(),Toast.LENGTH_LONG).show();
                cleverTapAPI.pushProfile(kv);
            }
        });


    }
}