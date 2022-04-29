package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.clevertapintegrationsample.notificationAPI.Android;
import com.example.clevertapintegrationsample.notificationAPI.Content;
import com.example.clevertapintegrationsample.notificationAPI.NotificationRequest;
import com.example.clevertapintegrationsample.notificationAPI.NotificationResponse;
import com.example.clevertapintegrationsample.notificationAPI.PlatformSpecific;
import com.example.clevertapintegrationsample.notificationAPI.RetrofitAPI;
import com.example.clevertapintegrationsample.notificationAPI.To;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DisplayUnitListener, CTInboxListener {

    TextView nativeText;
    ImageView nativeImageView;
    EditText identityEdt, emailEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(CleverTapAPI.getDefaultInstance(this)).setDisplayUnitListener(this);

        identityEdt = findViewById(R.id.identityEdt);
        emailEdt = findViewById(R.id.emailEdt);

        nativeText = findViewById(R.id.nativeText);
        nativeImageView = findViewById(R.id.nativeImage);

        /*CleverTapAPI.getDefaultInstance(this).setInAppNotificationButtonListener(new InAppNotificationButtonListener() {
            @Override
            public void onInAppButtonClick(HashMap<String, String> payload) {

            }
        });*/

        findViewById(R.id.sendData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = TextUtils.isEmpty(emailEdt.getText()) ? "":emailEdt.getText().toString();
                String identity = TextUtils.isEmpty(identityEdt.getText()) ? "14011995":identityEdt.getText().toString();
                MyApplication.getInstance().sendProfileData(identity,email);
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

        findViewById(R.id.apiCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });

        CleverTapAPI.getDefaultInstance(this).setCTNotificationInboxListener(this);
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

        Log.d("TAG", "postData() called"+new Gson().toJson(request));

        // calling a method to create a post and passing our modal class.
        Call<NotificationResponse> call = retrofitAPI.createPost("R9K-Z94-R46Z","ERM-ZUA-MAUL",
                "application/json",
                request);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null){
                    Log.d("TAG", "onResponse() called with: call = [" + call + "], " +
                            "response = [" + new Gson().toJson(response.body() )+ "]");
                }
                if (response.errorBody()!=null){
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
        for (CleverTapDisplayUnit cleverTapDisplayUnit: units) {
            //CUstomKV
            Map<String,String> customMap = cleverTapDisplayUnit.getCustomExtras();
            ArrayList<CleverTapDisplayUnitContent> contents = cleverTapDisplayUnit.getContents();
            for (CleverTapDisplayUnitContent content: contents) {
                String title = content.getTitle();
                String message = content.getMessage();
                String mediaUrl = content.getMedia();
                Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + title + "]");
                Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + message+ "]");
                Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + mediaUrl+ "]");

                nativeText.setText(title+" "+message);
            }
        }
    }

    @Override
    public void inboxDidInitialize() {

    }

    @Override
    public void inboxMessagesDidUpdate() {

    }
}