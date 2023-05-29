package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PushTemplateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_template);


        findViewById(R.id.ptBasic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Basic Trigger");
            }
        });
        findViewById(R.id.pt2C2A).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT 2CTA Trigger");
            }
        });
        findViewById(R.id.ptZeroBezel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Zero Bezel Trigger");
            }
        });
        findViewById(R.id.ptZeroBezelRM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Zero Bezel RM Trigger");
            }
        });

        findViewById(R.id.ptManualCarousel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Manual Carousel Trigger");
            }
        });

        findViewById(R.id.ptAutoCarousel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Auto Carousel Trigger");
            }
        });

        findViewById(R.id.ptSticky5Icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Sticky 5 Icon");
            }
        });

        findViewById(R.id.ptStickyCustom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Sticky Custom");
            }
        });
    }
}