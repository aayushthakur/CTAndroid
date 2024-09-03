package com.example.clevertapintegrationsample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.CleverTapAPI;

import java.util.HashMap;

public class CustomClevertapID extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clevertap_id);

        EditText editText = findViewById(R.id.customid);

        findViewById(R.id.onUserLoginCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText() != null || TextUtils.isEmpty(editText.getText().toString())) {
                    String customId = editText.getText().toString();
                    CleverTapAPI.changeCredentials(BuildConfig.ACCOUNT_ID,BuildConfig.CLEVERTAP_TOKEN);
                    CleverTapAPI clevertapDefaultInstance =
                            CleverTapAPI.getDefaultInstance(getApplicationContext(), customId);

                    if (!clevertapDefaultInstance.isPushPermissionGranted()) {
                        clevertapDefaultInstance.promptForPushPermission(true);
                    }
                    CleverTapAPI.setNotificationHandler(new PushTemplateNotificationHandler());
                    CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);

                    HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                    profileUpdate.put("Name", "Jack Montana");    // String
                    profileUpdate.put("Identity", customId);      // String or number
                    clevertapDefaultInstance.onUserLogin(profileUpdate, customId);
                }
            }
        });

    }
}