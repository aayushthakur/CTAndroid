package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DisplayUnitListener {

    TextView nativeText;
    ImageView nativeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(CleverTapAPI.getDefaultInstance(this)).setDisplayUnitListener(this);

        nativeText = findViewById(R.id.nativeText);
        nativeImageView = findViewById(R.id.nativeImage);

        findViewById(R.id.sendData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().sendProfileData();
            }
        });

        findViewById(R.id.sendInApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().sendInApp();
            }
        });

        findViewById(R.id.sendInAppInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().sendInAppInterstitial();
            }
        });

        findViewById(R.id.live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().sendLiveEvent();
            }
        });

        findViewById(R.id.updateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().updateProfile();
            }
        });
    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        for (CleverTapDisplayUnit cleverTapDisplayUnit: units) {
            Map<String,String> customMap = cleverTapDisplayUnit.getCustomExtras();
            ArrayList<CleverTapDisplayUnitContent> contents = cleverTapDisplayUnit.getContents();
            for (CleverTapDisplayUnitContent content: contents) {
                String title = content.getTitle();
                String message = content.getMessage();
                String mediaUrl = content.getMedia();
                nativeText.setText(title+" "+message);

            }
        }
    }
}