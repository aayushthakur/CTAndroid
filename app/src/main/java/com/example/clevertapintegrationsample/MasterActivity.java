package com.example.clevertapintegrationsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.clevertap.android.sdk.inapp.CTLocalInApp;
import com.example.clevertapintegrationsample.notificationAPI.PushPrimerActivity;

import org.json.JSONObject;

public class MasterActivity extends AppCompatActivity implements PushPermissionResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_activity);

        MyApplication.getInstance().getClevertapDefaultInstance().registerPushPermissionNotificationResponseListener(this);

        findViewById(R.id.pushPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.getInstance().getClevertapDefaultInstance().isPushPermissionGranted()) {
                    JSONObject jsonObject = CTLocalInApp.builder().setInAppType(CTLocalInApp.InAppType.HALF_INTERSTITIAL).setTitleText("Get Notified").setMessageText("Please enable notifications on your device to use Push Notifications.").followDeviceOrientation(true).setPositiveBtnText("Allow").setNegativeBtnText("Cancel").setBackgroundColor("#ffffff").setBtnBackgroundColor("#32c4b3").setBtnBorderColor("#ffffff").setTitleTextColor("#000000").setMessageTextColor("#000000").setBtnTextColor("#ffffff").setBtnBorderRadius("5").setImageUrl("https://iili.io/deTzDs2.png").setFallbackToSettings(true).build();
                    MyApplication.getInstance().getClevertapDefaultInstance().promptPushPrimer(jsonObject);
                } else {
                    Toast.makeText(getApplicationContext(), "Notification Permission Already Given", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.pushTemplates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PushTemplateActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.inAppTemplates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InAppTemplatesActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onPushPermissionResponse(boolean accepted) {
        if (accepted) {
            //do stuff
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApplication.getInstance().getClevertapDefaultInstance() != null) {
            MyApplication.getInstance().getClevertapDefaultInstance().unregisterPushPermissionNotificationResponseListener(this);
        }
    }
}