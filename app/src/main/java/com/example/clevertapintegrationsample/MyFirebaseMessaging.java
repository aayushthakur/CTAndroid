package com.example.clevertapintegrationsample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = FirebaseMessagingService.class.getName();

    public MyFirebaseMessaging() {
        super();
    }

    @NonNull
    @Override
    protected Intent getStartCommandIntent(@NonNull Intent intent) {
        return super.getStartCommandIntent(intent);
    }

    @Override
    public void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("TAG", "onMessageReceived: "+new Gson().toJson(remoteMessage.getData()));
//        new CTFcmMessageHandler().processPushAmp(getApplicationContext(), remoteMessage);
        try {
            if (remoteMessage.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);

                if (info.fromCleverTap) {
                    boolean status = new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
                    Log.i("TAG", "onMessageReceived status: "+status);
                } else {
                    customRenderNotification(remoteMessage);
                    // not from CleverTap handle yourself or pass to another provider
                }
            }
        } catch (Throwable t) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t);
        }
//        boolean status = new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
//        Log.i("TAG", "onMessageReceived status: "+status);

        /*if (remoteMessage.getData().size()>0) {
            Map<String,String> data = remoteMessage.getData();
            String title = data.get("nt");
           String message = data.get("nm");
            MyApplication.getInstance().sendNotificationAppInbox(title,message);
            EventBus.getDefault().post(new MessageEvent(title,message));
        }*/

    }

    private void customRenderNotification(RemoteMessage remoteMessage){
        Log.d(TAG, "customRenderNotification() called with: remoteMessage = [" + remoteMessage + "]");
//// Get the layouts to use in the custom notification
//        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);

// Apply the layouts to the notification

        if (remoteMessage.getData()==null) {
            return;
        }
        String title = remoteMessage.getData().get("nt");
        String message = remoteMessage.getData().get("nm");
        String channelId = remoteMessage.getData().get("wzrk_cid");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder customNotification = new NotificationCompat.Builder(getApplicationContext(),
                Objects.requireNonNull(channelId))
                .setSmallIcon(R.drawable.gcm_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle(title)
                .setContentText(message)
                .setColor(getResources().getColor(R.color.gray))
                .setColorized(true)
                .setContentIntent(contentIntent);
//                .setCustomContentView(notificationLayout)
//                .setCustomBigContentView(notificationLayoutExpanded)


        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, customNotification.build());
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
    }
}
