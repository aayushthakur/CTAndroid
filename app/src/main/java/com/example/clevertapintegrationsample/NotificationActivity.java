package com.example.clevertapintegrationsample;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class NotificationActivity extends AppCompatActivity {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    private static final String TAG = NotificationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
        Bundle bundle = getIntent().getExtras();

        //Method .pushNotificationViewedEvent(extras) to raise notificaiton clicked event
        MyApplication.getInstance().getClevertapDefaultInstance().pushNotificationClickedEvent(bundle);

        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context, Bundle bundle) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtras(bundle);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

}
