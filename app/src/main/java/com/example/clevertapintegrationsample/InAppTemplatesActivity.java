package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class InAppTemplatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_templates);


        findViewById(R.id.inAppCustomHtml).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Custom HTML");
            }
        });

        findViewById(R.id.inAppRatingTemplate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Ratings");
            }
        });

        findViewById(R.id.inAppSpinTemplate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Spin the wheel");
            }
        });

        findViewById(R.id.inAppHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Header");
            }
        });

        findViewById(R.id.inAppVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Video");
            }
        });
    }
}