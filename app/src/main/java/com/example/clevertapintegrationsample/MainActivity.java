package com.example.clevertapintegrationsample;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.clevertap.android.geofence.CTGeofenceAPI;
import com.clevertap.android.geofence.CTGeofenceSettings;
import com.clevertap.android.geofence.Logger;
import com.clevertap.android.geofence.interfaces.CTGeofenceEventsListener;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.clevertap.android.sdk.inbox.CTInboxMessage;
import com.example.clevertapintegrationsample.appinbox.CustomAppInboxActivity;
import com.example.clevertapintegrationsample.homescreen.ProductExperienceActivity;
import com.example.clevertapintegrationsample.nativeDisplay.NativeDisplayActivity;
import com.example.clevertapintegrationsample.notificationAPI.Android;
import com.example.clevertapintegrationsample.notificationAPI.Content;
import com.example.clevertapintegrationsample.notificationAPI.NotificationRequest;
import com.example.clevertapintegrationsample.notificationAPI.NotificationResponse;
import com.example.clevertapintegrationsample.notificationAPI.PlatformSpecific;
import com.example.clevertapintegrationsample.notificationAPI.PushPrimerActivity;
import com.example.clevertapintegrationsample.notificationAPI.RetrofitAPI;
import com.example.clevertapintegrationsample.notificationAPI.To;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DisplayUnitListener, CTInboxListener, InAppNotificationButtonListener /*InAppNotificationButtonListener, InAppListener*/ {

    // Unique integers to identify our requests
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int BACKGROUND_LOCATION_PERMISSION_CODE = 101;
    private static final String TAG = MainActivity.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 9999;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    private AlertDialog dialog;


    TextView nativeText;
    ImageView nativeImageView;
    EditText identityEdt, emailEdt;
    Button inbox, customInbox;
    CleverTapAPI cleverTapDefaultInstance;
    LinearLayoutCompat rootView;
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Add same code that you want to add in onActivityResult method
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes

                    }
                }
            });

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /**
         * On Android 12, clear notification on CTA click when Activity is already running in activity backstack
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            NotificationUtils.dismissNotification(intent, getApplicationContext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().getClevertapDefaultInstance().setDisplayUnitListener(this);
        checkAndRequestPermissions();

        rootView = findViewById(R.id.rootView);
        identityEdt = findViewById(R.id.identityEdt);
        emailEdt = findViewById(R.id.emailEdt);

        nativeText = findViewById(R.id.nativeText);
        nativeImageView = findViewById(R.id.nativeImage);
        inbox = findViewById(R.id.inbox);
        customInbox = findViewById(R.id.customInbox);


        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);

        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();

            initializeGeoFenceSDK();
        }


        if (cleverTapDefaultInstance != null) {


            cleverTapDefaultInstance.setInAppNotificationButtonListener(this);
            /*cleverTapDefaultInstance.setInAppNotificationButtonListener(payload -> {
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

            /*cleverTapDefaultInstance.setInAppNotificationListener(new InAppNotificationListener() {
                @Override
                public boolean beforeShow(Map<String, Object> extras) {
                    Log.d(TAG, "In App beforeShow() called with: extras = [" + extras + "]");
                    return true;                    //return true to show the inApp notification, if false then inapp wont show
                }

                @Override
                public void onDismissed(Map<String, Object> extras, @Nullable Map<String, Object> actionExtras) {
                    Log.d(TAG, "In App onDismissed() called with: extras = [" + extras + "], actionExtras = [" + actionExtras + "]");
                }
            });*/
        }

       /* CTGeofenceAPI.getInstance(getApplicationContext())
                .setOnGeofenceApiInitializedListener(new CTGeofenceAPI.OnGeofenceApiInitializedListener() {
                    @Override
                    public void OnGeofenceApiInitialized() {
                        //App is notified on the main thread that CTGeofenceAPI is initialized
                        Log.d(TAG, "OnGeofenceApiInitialized() called");
                    }
                });*/

       /* CTGeofenceAPI.getInstance(getApplicationContext())
                .setCtGeofenceEventsListener(new CTGeofenceEventsListener() {
                    @Override
                    public void onGeofenceEnteredEvent(JSONObject jsonObject) {
                        //Callback on the main thread when the user enters Geofence with info in jsonObject
                        Log.d(TAG, "onGeofenceEnteredEvent() called with: jsonObject = [" + new Gson().toJson(jsonObject) + "]");
                    }

                    @Override
                    public void onGeofenceExitedEvent(JSONObject jsonObject) {
                        //Callback on the main thread when user exits Geofence with info in jsonObject
                        Log.d(TAG, "onGeofenceExitedEvent() called with: jsonObject = [" + new Gson().toJson(jsonObject) + "]");

                    }
                });*/

       /* CTGeofenceAPI.getInstance(getApplicationContext())
                .setCtLocationUpdatesListener(new CTLocationUpdatesListener() {
                    @Override
                    public void onLocationUpdates(Location location) {
                        //New location on the main thread as provided by the Android OS
                        Log.d(TAG, "onLocationUpdates() called with: location = [" + location.toString() + "]");
                    }
                });*/

        findViewById(R.id.sendData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = TextUtils.isEmpty(emailEdt.getText()) ? "" : emailEdt.getText().toString();
                String identity = TextUtils.isEmpty(identityEdt.getText()) ? "" : identityEdt.getText().toString();
                MyApplication.getInstance().onUserLogin(identity, email);
            }
        });

        findViewById(R.id.sendDateEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> evtData = new HashMap<>();
                evtData.put("dateProp", new Date());
                DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = sourceFormat.parse("14/01/1995");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                evtData.put("dateProp1", date);
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("Date Event", evtData);
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

        findViewById(R.id.live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().sendLiveEvent();
            }
        });


        findViewById(R.id.webhookEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put("title", "Hi");
                data.put("message", "Whats Up");
                data.put("extraMessage", "Yo");
                data.put("deepLink", "https://app.cttest.com/native");
                data.put("bgColor", "#4aa66a");
                data.put("titleColor", "#ffffff");
                data.put("messageColor", "#ffffff");
                data.put("imageUrl", "https://i.ibb.co/N734CBv/88.jpg");

                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("Webhook Event",data);
            }
        });

        findViewById(R.id.updateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.productExperience).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProductExperienceActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.apiCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });

        findViewById(R.id.actionInbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().sendAppInboxTrigger();
            }
        });

        findViewById(R.id.soundNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Sound Notif");
            }
        });

        findViewById(R.id.lowImportanceNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("PT Low Importance Notif");
            }
        });

        findViewById(R.id.transactionSuccessful).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().chargedEvent();
            }
        });

        findViewById(R.id.nativeDisplayTrigger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("Native Display Trigger");
                Intent intent = new Intent(getApplicationContext(), NativeDisplayActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.pushTemplateSamples).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PushTemplateActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.copyCtId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cleverTapDefaultInstance != null && cleverTapDefaultInstance.getCleverTapID() != null) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("CT Id", cleverTapDefaultInstance.getCleverTapID());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "CT id " + cleverTapDefaultInstance.getCleverTapID() + " Copied to clipboard", Toast.LENGTH_LONG).show();
                }
            }
        });


        findViewById(R.id.inAppSamples).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InAppTemplatesActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.customCTID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomClevertapID.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.openProfilePage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.eventPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.pushPrimers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PushPrimerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cartAbandon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartAbandon.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.openWebview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Webview.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.registerPush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();
                                Log.v("TAG", "token: " + token);
                                MyApplication.getInstance().getClevertapDefaultInstance().pushFcmRegistrationId(token, true);
                                // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.unregisterPush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();
                                Log.v("TAG", "token: " + token);
                                MyApplication.getInstance().getClevertapDefaultInstance().pushFcmRegistrationId(token, false);
                                // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        checkPermissionOverlay();

        MyApplication.getInstance().getClevertapDefaultInstance().recordScreen("Home");

//        Map<String,Object> data = new HashMap<>();
//        data.put("float_user_property",1.5f);
//        MyApplication.getInstance().getClevertapDefaultInstance().pushProfile(data);

        cleverTapDefaultInstance.registerPushPermissionNotificationResponseListener(new PushPermissionResponseListener() {
            @Override
            public void onPushPermissionResponse(boolean accepted) {
                if (accepted){
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            Log.v("FirebaseMessaging", "token: " + token);
                            MyApplication.getInstance().getClevertapDefaultInstance().pushFcmRegistrationId(token, true);
                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("MSG-push", true);
                            MyApplication.getInstance().getClevertapDefaultInstance().pushProfile(data1);
                        }
                    });
                }
            }
        });


      /*  NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        String GROUP_KEY_NEWS_SPORTS = "news_sports";
        Notification newMessageNotification =
                new NotificationCompat.Builder(getApplicationContext(), "GroupChannel1")
                .setContentTitle("Sports News")
                .setContentText("FC Barcelona wins El Clasico!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setGroup(GROUP_KEY_NEWS_SPORTS)
                .build();


        notificationManager.notify(1, newMessageNotification);

        Notification newMessageNotification11 =
                new NotificationCompat.Builder(getApplicationContext(), "GroupChannel1")
                        .setContentTitle("Sports News")
                        .setContentText("Lakers defeat Warriors")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setGroup(GROUP_KEY_NEWS_SPORTS)
                        .build();


        notificationManager.notify(1, newMessageNotification11);

       *//* Notification summaryNotification =
                new NotificationCompat.Builder(getApplicationContext(), "GroupChannel1")
                .setContentTitle("Sports News Summary")
                .setContentText("2 new sports updates")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("FC Barcelona wins El Clasico!")
                        .addLine("Lakers defeat Warriors"))
                .setGroup(GROUP_KEY_NEWS_SPORTS)
                .setGroupSummary(true)
                .build();
        notificationManager.notify(1, summaryNotification);*//*

        String GROUP_KEY_NEWS_GLOBAL = "news_global";
        Notification newMessageNotification1 =
                new NotificationCompat.Builder(getApplicationContext(), "GroupChannel2")
                .setContentTitle("Global News")
                .setContentText("China Releases DeepSeek AI!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setGroup(GROUP_KEY_NEWS_GLOBAL)
                .build();

        notificationManager.notify(2, newMessageNotification1);


        Notification newMessageNotification2 =
                new NotificationCompat.Builder(getApplicationContext(), "GroupChannel2")
                        .setContentTitle("Global News")
                        .setContentText("Tesla Stock Price Go Down")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setGroup(GROUP_KEY_NEWS_GLOBAL)
                        .build();

        notificationManager.notify(2, newMessageNotification2);*/

        /*Notification summaryNotification1 =
                new NotificationCompat.Builder(getApplicationContext(), "GroupChannel2")
                .setContentTitle("Global News Summary")
                .setContentText("2 new global updates")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("China Releases DeepSeek AI!")
                        .addLine("Tesla Stock Price Go Down"))
                .setGroup(GROUP_KEY_NEWS_GLOBAL)
                .setGroupSummary(true)
                .build();
        notificationManager.notify(2, summaryNotification1);*/

    }

    private void checkAndRequestPermissions() {
        // Step 1: Check if we already have FINE location (Foreground)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not, request FINE location first
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        } else {
            // If we already have foreground, check/request background
            checkBackgroundLocation();
        }
    }

    private void checkBackgroundLocation() {
        // Background location only exists on Android 10 (API 29) and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // OPTIONAL: Show an alert dialog here explaining WHY you need background location.
                // Android 11+ requires the user to go to settings manually if they don't select "Allow all the time" immediately.

                // Request Background Location
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        BACKGROUND_LOCATION_PERMISSION_CODE);
            } else {
                Toast.makeText(this, "Background Location Already Granted", Toast.LENGTH_SHORT).show();
                // Do your background work here
            }
        } else {
            // For Android 9 and below, background location is granted with Fine Location
            Toast.makeText(this, "Background Location Granted (Legacy)", Toast.LENGTH_SHORT).show();
        }
    }

    // This method handles the user's choice (Allow/Deny)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            // Step 2: Handle result of Foreground Request
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Foreground Location Granted", Toast.LENGTH_SHORT).show();
                // Now that we have foreground, ask for background
                checkBackgroundLocation();
            } else {
                Toast.makeText(this, "Foreground Location Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == BACKGROUND_LOCATION_PERMISSION_CODE) {
            // Step 3: Handle result of Background Request
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Background Location Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Background Location Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                if (!checkPermission()) {
                    requestPermission();
                }
            }
        } catch (Exception ignored) {
        }
    }

    @RequiresApi(api = 33)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = 33)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), POST_NOTIFICATIONS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void postData() {

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.clevertap.com/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        // passing data from our text fields to our modal class.
        NotificationRequest request = new NotificationRequest();

        To toObject = new To();
        List<String> objs = new ArrayList<>();
        objs.add(CleverTapAPI.getDefaultInstance(getApplicationContext()).getCleverTapID());
        toObject.setObjectId(objs);
        request.setTo(toObject);

        Content content = new Content();
        content.setTitle("Title");
        content.setBody("Body");

        PlatformSpecific platformSpecific = new PlatformSpecific();
        Android android = new Android();
        android.setWzrkCid("testChannelId1");
        platformSpecific.setAndroid(android);
        content.setPlatformSpecific(platformSpecific);
        request.setContent(content);

        request.setTagGroup("Tag group");
        request.setRespectFrequencyCaps(false);
        request.setName("Android Test Campaign API");

        Log.d("TAG", "postData() called" + new Gson().toJson(request));

        // calling a method to create a post and passing our modal class.
        Call<NotificationResponse> call = retrofitAPI.createPost("R9K-Z94-R46Z", "ERM-ZUA-MAUL",
                "application/json",
                request);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null) {
                    Log.d("TAG", "onResponse() called with: call = [" + call + "], " +
                            "response = [" + new Gson().toJson(response.body()) + "]");
                }
                if (response.errorBody() != null) {
                    Log.d("TAG", "onResponse() called with: call = [" + call + "]," +
                            " response = [" + new Gson().toJson(response.errorBody()) + "]");
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t.getMessage() + "]");
            }
        });

    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + units.toString() + "]");
        for (CleverTapDisplayUnit cleverTapDisplayUnit : units) {
            ArrayList<CleverTapDisplayUnitContent> contents = cleverTapDisplayUnit.getContents();
            for (CleverTapDisplayUnitContent content : contents) {
                String title = content.getTitle();
                String message = content.getMessage();
                Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + title + "]");
                Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + message + "]");
                nativeText.setText(title + " " + message);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!cleverTapDefaultInstance.isPushPermissionGranted()) {
            Log.d(TAG, "onCreate() called with: pusPermissionGranted = [" + cleverTapDefaultInstance.isPushPermissionGranted() + "]");
            cleverTapDefaultInstance.promptForPushPermission(true);
        }
    }

    @Override
    public void inboxDidInitialize() {
        Log.d("TAG", "inboxDidInitialize() called " + cleverTapDefaultInstance.getInboxMessageCount());
//        Log.d("TAG", "inboxDidInitialize() called "+new Gson().toJson(cleverTapDefaultInstance.getAllInboxMessages()));
        //dismiss open
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To Open Default App Inbox
                showAppInbox();

                //To Open Custom App Inbox
//                Intent intent = new Intent(getApplicationContext(), CustomAppInboxActivity.class);
//                ArrayList<CTInboxMessage> inboxMessages = cleverTapDefaultInstance.getAllInboxMessages();
//                intent.putParcelableArrayListExtra("app_inbox_messages",inboxMessages);
//                startActivity(intent);
            }
        });

        customInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //To Open Custom App Inbox
                Intent intent = new Intent(getApplicationContext(), CustomAppInboxActivity.class);
                ArrayList<CTInboxMessage> inboxMessages = cleverTapDefaultInstance.getAllInboxMessages();
                intent.putParcelableArrayListExtra("app_inbox_messages",inboxMessages);
                startActivity(intent);
            }
        });
    }

    private void showAppInbox() {
//        ArrayList<String> tabs = new ArrayList<>();
//        tabs.add("Promotions");//We support upto 2 tabs only. Additional tabs will be ignored
//        tabs.add("Promotions");

        CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
//        styleConfig.setFirstTabTitle("TestFilterTag");
//        styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
        styleConfig.setTabBackgroundColor("#FFFFFF");
        styleConfig.setSelectedTabIndicatorColor("#3498DB");
        styleConfig.setSelectedTabColor("#3498DB");
        styleConfig.setUnselectedTabColor("#808B96");
        styleConfig.setBackButtonColor("#3498DB");
        styleConfig.setNavBarTitleColor("#3498DB");
        styleConfig.setNavBarTitle("MY INBOX");
        styleConfig.setNavBarColor("#FFFFFF");
        styleConfig.setInboxBackgroundColor("#85C1E9");
        if (cleverTapDefaultInstance != null) {
            cleverTapDefaultInstance.showAppInbox(styleConfig); //With Tabs
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // Do something
        showAlertDialogButtonClicked(event.title, event.messge);
    }

    public void showAlertDialogButtonClicked(String title, String message) {

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("App Inbox Alert");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_custom_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        TextView tvTitle = customLayout.findViewById(R.id.title);
        tvTitle.setText(title);
        TextView tvMessage = customLayout.findViewById(R.id.message);
        tvMessage.setText(message);

        customLayout.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppInbox();
                dialog.dismiss();
            }
        });

        customLayout.findViewById(R.id.dismissButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void inboxMessagesDidUpdate() {
        Log.d("TAG", "inboxMessagesDidUpdate() called " + cleverTapDefaultInstance.getInboxMessageCount());
        Log.d("TAG", "inboxMessagesDidUpdate() called " + new Gson().toJson(cleverTapDefaultInstance.getAllInboxMessages()));
    }

    public void checkPermissionOverlay() {
        boolean isTelevision = getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK);
        if (isTelevision) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!MyApplication.getInstance().isOverlayPermissionGiven()) {
                    requestOverlayDisplayPermission();

                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestOverlayDisplayPermission() {
        // An AlertDialog is created
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // This dialog can be closed, just by
        // taping outside the dialog-box
        builder.setCancelable(true);

        // The title of the Dialog-box is set
        builder.setTitle("Screen Overlay Permission Needed");

        // The message of the Dialog-box is set
        builder.setMessage("Enable 'Display over other apps' from System Settings.");

        // The event of the Positive-Button is set
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityIntent.launch(intent);
                }
                catch (ActivityNotFoundException e){
                    Snackbar.make(rootView,"Go to TV Settings -> Apps- > Special App Access", Snackbar.LENGTH_LONG)
                            .show();
//                        Toast.makeText(getApplicationContext(),"Go to TV Settings -> Apps- > Special App Access", Toast.LENGTH_LONG).show();
                    }
            }
        });
        dialog = builder.create();
        // The Dialog will show in the screen
        dialog.show();
    }

    @Override
    public void onInAppButtonClick(HashMap<String, String> payload) {
        Log.d(TAG, "onInAppButtonClick() called with: payload = [" + payload + "]");
    }

   /* @Override
    public boolean beforeShow(Map<String, Object> extras) {
        Log.d(TAG, "beforeShow() called with: extras = [" + extras + "]");
        return false;
    }

    @Override
    public void onDismissed(Map<String, Object> extras, @Nullable Map<String, Object> actionExtras) {
        Log.d(TAG, "onDismissed() called with: extras = [" + extras + "], actionExtras = [" + actionExtras + "]");
    }*/

   /* @Override
    public void inAppNotificationDidClick(CTInAppNotification inAppNotification, Bundle formData, HashMap<String, String> keyValueMap) {
        Log.d(TAG, "inAppNotificationDidClick() called with: inAppNotification = [" + inAppNotification + "], formData = [" + formData + "], keyValueMap = [" + keyValueMap + "]");
    }

    @Override
    public void inAppNotificationDidDismiss(Context context, CTInAppNotification inAppNotification, Bundle formData) {
        Log.d(TAG, "inAppNotificationDidDismiss() called with: context = [" + context + "], inAppNotification = [" + inAppNotification + "], formData = [" + formData + "]");
    }

    @Override
    public void inAppNotificationDidShow(CTInAppNotification inAppNotification, Bundle formData) {
        Log.d(TAG, "inAppNotificationDidShow() called with: inAppNotification = [" + inAppNotification + "], formData = [" + new Gson().toJson(formData) + "]");
    }

    @Override
    public void onInAppButtonClick(HashMap<String, String> payload) {
        Log.d(TAG, "onInAppButtonClick() called with: payload = [" + payload + "]");
    }*/

    private void initializeGeoFenceSDK(){
        CTGeofenceSettings ctGeofenceSettings = new CTGeofenceSettings.Builder()
                .enableBackgroundLocationUpdates(true)//boolean to enable background location updates
                .setLogLevel(Logger.VERBOSE)//Log Level
                .setLocationAccuracy(CTGeofenceSettings.ACCURACY_HIGH)//byte value for Location Accuracy
                .setLocationFetchMode(CTGeofenceSettings.FETCH_CURRENT_LOCATION_PERIODIC)//byte value for Fetch Mode
                .setGeofenceMonitoringCount(CTGeofenceSettings.DEFAULT_GEO_MONITOR_COUNT)//int value for number of Geofences CleverTap can monitor
                .build();

        CTGeofenceAPI.getInstance(getApplicationContext()).init(ctGeofenceSettings, cleverTapDefaultInstance);
        try {
            CTGeofenceAPI.getInstance(getApplicationContext()).triggerLocation();
        } catch (IllegalStateException e) {
            // thrown when this method is called before geofence SDK initialization
        }

        CTGeofenceAPI.getInstance(getApplicationContext())
                .setOnGeofenceApiInitializedListener(() -> {
                    //App is notified on the main thread that CTGeofenceAPI is initialized
                    Log.d("CTGEOFENCELOG", "OnGeofenceApiInitialized() called");
                });

        CTGeofenceAPI.getInstance(getApplicationContext())
                .setCtGeofenceEventsListener(new CTGeofenceEventsListener() {
                    @Override
                    public void onGeofenceEnteredEvent(JSONObject jsonObject) {
                        //Callback on the main thread when the user enters Geofence with info in jsonObject
                        Log.d("CTGEOFENCELOG", "onGeofenceEnteredEvent() called with: jsonObject = [" + new Gson().toJson(jsonObject) + "]");
                    }

                    @Override
                    public void onGeofenceExitedEvent(JSONObject jsonObject) {
                        //Callback on the main thread when user exits Geofence with info in jsonObject
                        Log.d("CTGEOFENCELOG", "onGeofenceExitedEvent() called with: jsonObject = [" + new Gson().toJson(jsonObject) + "]");

                    }
                });

        CTGeofenceAPI.getInstance(getApplicationContext())
                .setCtLocationUpdatesListener(location -> {
                    //New location on the main thread as provided by the Android OS
                    Log.d("CTGEOFENCELOG", "onLocationUpdates() called with: location = [" + location.toString() + "]");
                });
    }


}