package com.example.clevertapintegrationsample;

import android.app.Application;
import android.app.NotificationManager;
import android.content.res.Configuration;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.signature.ObjectKey;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    private CleverTapAPI clevertapDefaultInstance;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();
        singleton = this;
        // Required initialization logic here!
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);
        clevertapDefaultInstance.pushEvent("Aayush App Open");
//        String fcmRegId;
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.v("TAG", "token: "+token);
                        clevertapDefaultInstance.pushFcmRegistrationId(token,true);
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "testChannelId1","Test Channel 1",
                    "Test Channel Description",
                    NotificationManager.IMPORTANCE_MAX,true);
        }

    }

    public void sendProfileData(String identity, String email){
        // each of the below mentioned fields are optional
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Name", "Aayush Thakur");    // String
        profileUpdate.put("Identity", identity);      // String or number
        profileUpdate.put("Email", email); // Email address of the user
        profileUpdate.put("Phone", "+917737388313");   // Phone (with the country code, starting with +)
        profileUpdate.put("Gender", "M");             // Can be either M or F
        profileUpdate.put("DOB", new Date(1995,0,14));         // Date of Birth. Set the Date object to the appropriate value first

        clevertapDefaultInstance.onUserLogin(profileUpdate);
    }

    public void updateProfile(){
        Double d = 100.67;
        // each of the below mentioned fields are optional
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Employed", "Y");                         // Can be either Y or N
        profileUpdate.put("Education", "Graduate");
        profileUpdate.put("Married", "Y" );         // Date of Birth. Set the Date object to the appropriate value first
        profileUpdate.put("Custom Score", 100 );
        profileUpdate.put("Custom Score String", "100.09" );
        profileUpdate.put("Custom Score Double", 100.10 );
        profileUpdate.put("Custom Score Double Var", d );
        profileUpdate.put("Custom Score Float", 100.89f );
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("tn:2");
        arrayList.add("tn:24454");
        arrayList.add("pl:489889");
        arrayList.add("pl:4898896464");
        profileUpdate.put("pn_scores",arrayList);
        clevertapDefaultInstance.pushProfile(profileUpdate);
    }

    public void sendInApp(){
        clevertapDefaultInstance.pushEvent("Aayush InApp");
    }

    public void sendInAppInterstitial(){
        //oneplus 7
        //apple iphone 13
        Map<String,Object> data = new HashMap<>();
        data.put("Name", "onpleus");
        data.put("productid", "23786487326");
        data.put("color", "blue");

        clevertapDefaultInstance.pushEvent("Product viewed",data);

        clevertapDefaultInstance.pushEvent("Aayush InApp Interstitial",new ArrayMap<>());

    }


    public void sendAppInboxTrigger(){
        clevertapDefaultInstance.pushEvent("App Inbox Trigger");
    }

    public void sendLiveEvent(){
        clevertapDefaultInstance.pushEvent("iamlive");
    }

    private static MyApplication singleton;

    public static MyApplication getInstance() {
        return singleton;
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
