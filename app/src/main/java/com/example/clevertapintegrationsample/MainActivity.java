package com.example.clevertapintegrationsample;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Activity;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.clevertap.android.sdk.inbox.CTInboxMessage;
import com.example.clevertapintegrationsample.appinbox.CustomAppInboxActivity;
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

    private static final String TAG = MainActivity.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 9999;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    private AlertDialog dialog;


    TextView nativeText;
    ImageView nativeImageView;
    EditText identityEdt, emailEdt;
    Button inbox;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().getClevertapDefaultInstance().setDisplayUnitListener(this);

        rootView = findViewById(R.id.rootView);
        identityEdt = findViewById(R.id.identityEdt);
        emailEdt = findViewById(R.id.emailEdt);

        nativeText = findViewById(R.id.nativeText);
        nativeImageView = findViewById(R.id.nativeImage);
        inbox = findViewById(R.id.inbox);


        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);

        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();

            if (!cleverTapDefaultInstance.isPushPermissionGranted()) {
                cleverTapDefaultInstance.promptForPushPermission(true);
            }
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


}