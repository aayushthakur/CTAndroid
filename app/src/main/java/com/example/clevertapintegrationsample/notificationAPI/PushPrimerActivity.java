package com.example.clevertapintegrationsample.notificationAPI;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import static com.clevertap.android.sdk.PushPermissionManager.ANDROID_PERMISSION_STRING;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Constants;
import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.clevertap.android.sdk.inapp.CTLocalInApp;
import com.example.clevertapintegrationsample.MyApplication;
import com.example.clevertapintegrationsample.R;

import org.json.JSONObject;

public class PushPrimerActivity extends AppCompatActivity implements PushPermissionResponseListener {

    private static final int PERMISSION_REQUEST_CODE = 1111;
    private static final String TAG = PushPrimerActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_primer);

        MyApplication.getInstance().getClevertapDefaultInstance().registerPushPermissionNotificationResponseListener(this);

        findViewById(R.id.removePermission).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                openNotificationSettings();
            }
        });
        findViewById(R.id.androidOSDefault).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNotificationPermission();
            }
        });

        findViewById(R.id.osDefaultUsingCT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.getInstance().getClevertapDefaultInstance().isPushPermissionGranted()) {
                    MyApplication.getInstance().getClevertapDefaultInstance().promptForPushPermission(true);
                }else {
                    Toast.makeText(getApplicationContext(),"Notification Permission Already Given",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.halfPrimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.getInstance().getClevertapDefaultInstance().isPushPermissionGranted()) {
                JSONObject jsonObject = CTLocalInApp.builder()
                        .setInAppType(CTLocalInApp.InAppType.HALF_INTERSTITIAL)
                        .setTitleText("Get Notified")
                        .setMessageText("Please enable notifications on your device to use Push Notifications.")
                        .followDeviceOrientation(true)
                        .setPositiveBtnText("Allow")
                        .setNegativeBtnText("Cancel")
                        .setBackgroundColor("#ffffff")
                        .setBtnBackgroundColor("#32c4b3")
                        .setBtnBorderColor("#ffffff")
                        .setTitleTextColor("#000000")
                        .setMessageTextColor("#000000")
                        .setBtnTextColor("#ffffff")
                        .setBtnBorderRadius("5")
//                        .setImageUrl("https://i.ibb.co/6FG7Z2w/Screenshot-2023-09-12-at-11-38-2.png")
                        .setFallbackToSettings(true)
                        .build();
                MyApplication.getInstance().getClevertapDefaultInstance().promptPushPrimer(jsonObject);
                }else {
                    Toast.makeText(getApplicationContext(),"Notification Permission Already Given",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.alertPrimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.getInstance().getClevertapDefaultInstance().isPushPermissionGranted()) {
                    JSONObject jsonObject = CTLocalInApp.builder()
                            .setInAppType(CTLocalInApp.InAppType.ALERT)
                            .setTitleText("Get Notified")
                            .setMessageText("Enable Notification permission")
                            .followDeviceOrientation(true)
                            .setPositiveBtnText("Allow")
                            .setNegativeBtnText("Cancel")
                            .build();
                    MyApplication.getInstance().getClevertapDefaultInstance().promptPushPrimer(jsonObject);
                }else {
                    Toast.makeText(getApplicationContext(),"Notification Permission Already Given",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                if (!checkPermission()){
                        requestPermission();
                }else {
                    Toast.makeText(getApplicationContext(),"Notification Permission Already Given",Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ignored){ }
    }

//    boolean shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(
//            this,
//            ANDROID_PERMISSION_STRING);

    @RequiresApi(api = 33)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = 33)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), POST_NOTIFICATIONS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPushPermissionResponse(boolean accepted) {
        Log.d(TAG, "onPushPermissionResponse() called with: accepted = [" + accepted + "]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApplication.getInstance().getClevertapDefaultInstance() != null) {
            MyApplication.getInstance().getClevertapDefaultInstance().unregisterPushPermissionNotificationResponseListener(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openNotificationSettings(){
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}