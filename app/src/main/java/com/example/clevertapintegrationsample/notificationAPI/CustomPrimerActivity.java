package com.example.clevertapintegrationsample.notificationAPI;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clevertapintegrationsample.R;

public class CustomPrimerActivity extends AppCompatActivity {

    TextView primerMessage,primerSkipButton;
    Button primerAllowButton;
    private static final int PERMISSION_REQUEST_CODE = 1495;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_primer);

        primerAllowButton = findViewById(R.id.primerAllowButton);
        primerMessage = findViewById(R.id.primerMessage);
        primerSkipButton = findViewById(R.id.primerSkipButton);

        primerSkipButton.setOnClickListener(view -> finish());



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

    @RequiresApi(api = 33)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = 33)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), POST_NOTIFICATIONS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openNotificationSettings(){
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}