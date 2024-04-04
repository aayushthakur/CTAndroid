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

        findViewById(R.id.inAppGIf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Custom GIF");
            }
        });

        findViewById(R.id.inAppScratchCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Scratch Card");
            }
        });

        findViewById(R.id.inAppCustomCarousel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Custom Carousel");
            }
        });
        findViewById(R.id.inAppCustomCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Custom Card Image");
            }
        });
        findViewById(R.id.inAppSlidingImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Sliding Image");
            }
        });

        findViewById(R.id.inAppBottomCornerGif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Bottom Corner Gif");
            }
        });

        findViewById(R.id.inAppFooterGif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Footer Gif");
            }
        });

        findViewById(R.id.youtubeVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("InApp Youtube Video");
            }
        });
    }
}