package com.example.clevertapintegrationsample;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.clevertap.android.geofence.CTGeofenceAPI;
import com.clevertap.android.geofence.CTGeofenceSettings;
import com.clevertap.android.geofence.Logger;
import com.clevertap.android.geofence.interfaces.CTGeofenceEventsListener;
import com.clevertap.android.geofence.interfaces.CTLocationUpdatesListener;
import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.pushtemplates.TemplateRenderer;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationListener;
import com.clevertap.android.sdk.inapp.CTInAppNotification;
import com.clevertap.android.sdk.interfaces.OnInitCleverTapIDListener;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks  /*CTPushAmpListener*/ {

    private static final String TAG = MyApplication.class.getName();
    private static MyApplication singleton;
    private CleverTapAPI clevertapDefaultInstance;

    public static MyApplication getInstance() {
        return singleton;
    }

    private FirebaseAnalytics mFirebaseAnalytics;


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
//        clearPrefAfterDeMerge();
        ActivityLifecycleCallback.register(this);
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        singleton = this;
        // Required initialization logic here!
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
//        clevertapDefaultInstance.setCTPushAmpListener(this);
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);

        TemplateRenderer.setDebugLevel(3);
        CleverTapAPI.setNotificationHandler(new PushTemplateNotificationHandler());

        if (clevertapDefaultInstance != null) {
            clevertapDefaultInstance.enablePersonalization();
            String customerName = (String) clevertapDefaultInstance.getProperty("Name");
            Log.d(TAG, "Property Name Value is : " + customerName);
        }

//        Map<String, Object> data = new HashMap<>();
//        data.put("sample_date", "01-02-2023");
//        clevertapDefaultInstance.pushEvent("Aayush App Open", data);
        clevertapDefaultInstance.setCTPushNotificationListener(new CTPushNotificationListener() {
            @Override
            public void onNotificationClickedPayloadReceived(HashMap<String, Object> payload) {
                Log.d(TAG, "onNotificationClickedPayloadReceived() called with: payload = [" + payload + "]");
                if (payload == null) {
                    return;
                }
                if (payload.containsKey("pt_id") && payload.get("pt_id").equals("pt_rating")) {
                    NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel((Integer) payload.get("notificationId"));
                }
                if (payload.containsKey("pt_id") && payload.get("pt_id").equals("pt_product_display")) {
                    NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel((Integer) payload.get("notificationId"));
                }

                final String COUPON_CODE_KEY = "coupon_code";

                // 2. Check if the payload contains the coupon code
                if (payload.containsKey(COUPON_CODE_KEY)) {

                    // Retrieve the coupon code value
                    String couponCode = (String) payload.get(COUPON_CODE_KEY);

                    if (couponCode != null && !couponCode.isEmpty()) {

                        Context context = getApplicationContext();
                        // Get the ClipboardManager system service
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // Create a new clip (data object for the clipboard)
                        // The first parameter is a user-visible label.
                        ClipData clip = ClipData.newPlainText("Coupon Code", couponCode);
                        // Set the clip to the system clipboard
                        clipboard.setPrimaryClip(clip);
                        // 3. Provide visual confirmation (Toast)
                        Toast.makeText(
                                context,
                                "Code '" + couponCode + "' copied to clipboard!",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {
                        // Handle case where key exists but value is null/empty
                        Log.e("CleverTap", "Coupon code key found, but value is empty.");
                    }
                } else {
                    // Handle case where notification was clicked but didn't contain the coupon_code key
                    Log.d("CleverTap", "Notification clicked, but no coupon code key found in payload.");
                }

            }
        });

//        String fcmRegId;
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        Log.v("TAG", "token: " + token);
//                        clevertapDefaultInstance.pushFcmRegistrationId(token, true);
//                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
////                        Log.d(TAG, msg);
////                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "testChannelId1", "Test Channel 1",
                    "Test Channel Description",
                    NotificationManager.IMPORTANCE_MAX, true);

            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "soundChannel", "Sound Channel",
                    "Channel with custom sound",
                    NotificationManager.IMPORTANCE_MAX, true, "anya.mp3");

            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "silentChannel", "Silent Channel",
                    "Channel with no sound",
                    NotificationManager.IMPORTANCE_MAX, true, "silent.mp3");

            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "lowImportance", "Low Importance Channel",
                    "Channel with low importance",
                    NotificationManager.IMPORTANCE_LOW, true);

            CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),
                    "GroupId1", "Group 1");

            CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),
                    "GroupId2", "Group 2");


            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "GroupChannel1", "Group Channel 1",
                    "Group Channel 1 Description",
                    NotificationManager.IMPORTANCE_MAX, "GroupId1", true);

            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "GroupChannel2", "Group Channel 2",
                    "Group Channel 2 Description",
                    NotificationManager.IMPORTANCE_MAX, "GroupId2", true);


//            NotificationChannel notificationChannel = new NotificationChannel("silentChannel","Silent Channel",NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setSound(null,null);

        }



        if (clevertapDefaultInstance != null) {

           /* clevertapDefaultInstance.setInAppNotificationButtonListener(payload -> {
                Log.d(TAG, "In App onInAppButtonClick() called with: payload = [" + payload + "]");
                if (payload != null && !payload.isEmpty()) {
                    if (payload.containsKey("title")) {
                        String inAppTitle = payload.get("title");
                        Log.d(TAG, "In App called inAppTitle = [" + inAppTitle + "]");
                    }
                    if (payload.containsKey("inapp_deeplink")) {
                        String deepLink = payload.get("inapp_deeplink");
                        Log.d(TAG, "In App called with: deepLink = [" + deepLink + "]");

                    }
                    if (payload.containsKey("extra_key")) {
                        String extraValue = payload.get("extra_key");
                        Log.d(TAG, "In App called with: extraValue = [" + extraValue + "]");
                    }
                }
            });*/


//            getNotificationPermission();

            clevertapDefaultInstance.setInAppNotificationListener(new InAppNotificationListener() {
                @Override
                public boolean beforeShow(Map<String, Object> extras) {
                    Log.d(TAG, "In App beforeShow() called with: extras = [" + new Gson().toJson(extras) + "]");
                    return true;                    //return true to show the inApp notification, if false then inapp wont show
                }

                @Override
                public void onShow(CTInAppNotification ctInAppNotification) {
                    Log.d(TAG, "In App onShow(): "+ new Gson().toJson(ctInAppNotification));
                }

                @Override
                public void onDismissed(Map<String, Object> extras, @Nullable Map<String, Object> actionExtras) {
                    Log.d(TAG, "In App onDismissed() called with: extras = [" + extras + "], actionExtras = [" + actionExtras + "]");
                }
            });
        }


      /*  JSONObject initOptions = new JSONObject();
        try {
            initOptions.put("accountId", "6568502216033fcfba4cfc76");
            initOptions.put("apiKey", "uXFsHxBseCIUgDqU5kKLpgLR6KO7ZUmY5ObZqs1p5ox4vlxxTKxfHUiewEhCZgTN");
            initOptions.put("cuid", "TEST_AAYUSH1495");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Create a Builder instance of SignedCallInitConfiguration and pass it inside the init() method
        SignedCallInitConfiguration initConfiguration = new SignedCallInitConfiguration.Builder(initOptions, false)
                .build();

        SignedCallAPI.getInstance().init(getApplicationContext(), initConfiguration, clevertapDefaultInstance, signedCallInitListener);
        SignedCallAPI.setDebugLevel(SignedCallAPI.LogLevel.VERBOSE);*/
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        clevertapDefaultInstance.getCleverTapID(cleverTapID -> {
            if (cleverTapID!=null){
                mFirebaseAnalytics.setUserProperty("ct_objectId", cleverTapID);
            }
        });
    }

    public void onUserLogin(String identity, String email) {
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Identity", identity);      // String or number
        profileUpdate.put("Email", email); // Email address of the user
        clevertapDefaultInstance.onUserLogin(profileUpdate);
    }

    public void updateProfile(HashMap<String, Object> profileHashMap) {
//        Double d = 100.67;
        // each of the below mentioned fields are optional
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Name", "SHELDON LEE COOPER");    // String
        profileUpdate.put("Employed", "Y");                         // Can be either Y or N
        profileUpdate.put("Education Status", "GRAD");
        profileUpdate.put("Married", "Y");         // Date of Birth. Set the Date object to the appropriate value first
//        profileUpdate.put("Custom Score", 100 );
//        profileUpdate.put("Custom Score String", "100.09" );
//        profileUpdate.put("Custom Score Double", 100.10 );
//        profileUpdate.put("Custom Score Double Var", d );
//        profileUpdate.put("Custom Score Float", 100.89f );
//        profileUpdate.put("Gender","Custom Gender");
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sourceFormat.parse("14/01/1995");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        profileUpdate.put("DOB", date);         // Date of Birth. Set the Date object to the appropriate value first
        /*ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("tn:2");
        arrayList.add("tn:24454");
        arrayList.add("pl:489889");
        arrayList.add("pl:4898896464");
        profileUpdate.put("pn_scores",arrayList);*/
        clevertapDefaultInstance.pushProfile(profileUpdate);
    }

    public CleverTapAPI getClevertapDefaultInstance() {
        return clevertapDefaultInstance;
    }

    public void sendInApp() {
        clevertapDefaultInstance.pushEvent("Aayush InApp");
    }

    public void sendInAppInterstitial() {
        //oneplus 7
        //apple iphone 13
        Map<String, Object> data = new HashMap<>();
        data.put("Name", "onpleus");
        data.put("productid", "23786487326");
        data.put("color", "blue");

        clevertapDefaultInstance.pushEvent("Product viewed", data);

        clevertapDefaultInstance.pushEvent("Aayush InApp Interstitial", new ArrayMap<>());

    }

    public void sendNotificationAppInbox(String title, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("message", message);
        clevertapDefaultInstance.pushEvent("Notification App Inbox", data);
    }

    public void sendAppInboxTrigger() {
        clevertapDefaultInstance.pushEvent("App Inbox Trigger");
    }

    public void chargedEvent() {
        HashMap<String, Object> chargeDetails = new HashMap<String, Object>();
        chargeDetails.put("Amount", 300);
        chargeDetails.put("Payment mode", "Credit card");
        chargeDetails.put("Charged ID", 24052013);

        HashMap<String, Object> item1 = new HashMap<String, Object>();
        item1.put("Product category", "books");
        item1.put("Book name", "The Millionaire next door");
        item1.put("Quantity", 1);

        HashMap<String, Object> item2 = new HashMap<String, Object>();
        item2.put("Product category", "books");
        item2.put("Book name", "Achieving inner zen");
        item2.put("Quantity", 1);

        HashMap<String, Object> item3 = new HashMap<String, Object>();
        item3.put("Product category", "books");
        item3.put("Book name", "Chuck it, let's do it");
        item3.put("Quantity", 5);

        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        try {
            clevertapDefaultInstance.pushChargedEvent(chargeDetails, items);
        } catch (Exception e) {
            // You have to specify the first parameter to push()
            // as CleverTapAPI.CHARGED_EVENT
        }
    }

    public void sendLiveEvent() {
        Map<String, Object> data = new HashMap<>();
        data.put("testKey", "testValue");
        clevertapDefaultInstance.pushEvent("iamlive", data);
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

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
        /**
         * On Android 12, clear notification on CTA click when Activity is already running in activity backstack
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            NotificationUtils.dismissNotification(activity.getIntent(), getApplicationContext());
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "onActivityStarted() called with: activity = [" + activity + "]");

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "onActivityResumed() called with: activity = [" + activity + "]");
        /*Bundle payload = activity.getIntent().getExtras();
        if (payload==null){
            return;
        }
        if (payload.containsKey("pt_id")&& payload.getString("pt_id").equals("pt_rating"))
        {
            NotificationManager nm = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(payload.getInt("notificationId"));
        }
        if (payload.containsKey("pt_id")&& payload.getString("pt_id").equals("pt_product_display"))
        {
            NotificationManager nm = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(payload.getInt("notificationId"));
        }*/
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "onActivityPaused() called with: activity = [" + activity + "]");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "onActivityStopped() called with: activity = [" + activity + "]");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d(TAG, "onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "onActivityDestroyed() called with: activity = [" + activity + "]");
    }

    public void clearPrefAfterDeMerge() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("WizRocket", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
    }

    public boolean isOverlayPermissionGiven() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;

    }

    /*@Override
    public void onPushAmpPayloadReceived(Bundle extras) {
        Log.d(TAG, "onPushAmpPayloadReceived() called with: extras = [" + extras + "]");
    }*/
}
