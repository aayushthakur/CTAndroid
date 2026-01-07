package com.example.clevertapintegrationsample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
    public static final String ACTION_1 = "action_1";
    public static final int RANDOM_NOTIFICATION_ID = 999;
    public static final String CURRENT_APP_NAME = "A23_POKER";
    private static final String TAG = FirebaseMessagingService.class.getName();
    int id = 1;

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

   /* @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        try {
            if (message.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
                if (info.fromCleverTap) {
                    //Render using Clevertap
                }else{
                   //Render using your own logic
                    Map<String,Object> eventProps = new HashMap<>();
                    eventProps.put("someprop","somevalue");
                   CleverTapAPI.getDefaultInstance(getApplicationContext()).pushEvent("Custom Impression Event",eventProps);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived: " + new Gson().toJson(remoteMessage.getData()));
        boolean isTelevision = getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK);

        try {
            if (remoteMessage.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
                if (info.fromCleverTap) {
                    if (extras.containsKey("app_to_render")) {
                        //app_to_render will be a custom key which will be passed by CRM team under key value pair
                        // for push notifications
                        // & the value will be the app name to which they want this notification to render on.
                        //the value should match with the one's fixed by the dev team in each app
                        String appName = extras.getString("app_to_render");
                        //here CURRENT_APP_NAME will be a static string saved in each app with its name like a23_poker,a23_rummy etc.
                        if (appName.equals(CURRENT_APP_NAME)) {

                            //render notification using
                            //new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
                        } else {
                            //this notification is meant for other application, do not render here just process notification
                            //using CleverTapAPI.processPushNotification(getApplicationContext(), extras);
                        }
                    } else {
                        //render normally using new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
                    }

                    if (isTelevision) {
                        Log.d(TAG, "Running on a TV Device");
                        //The below line helps to avoid duplicate rendering when using custom render implementation via CT
                        CleverTapAPI.processPushNotification(getApplicationContext(), extras);
                        ////////////////////////////////////////////////////
                        if (MyApplication.getInstance().isOverlayPermissionGiven()) {
                            NotifUtil.floatingNotif(this, remoteMessage);
                        }

                    } else {
                        Log.d(TAG, "Running on a non-TV Device");
                        if (extras.containsKey("isSticky")) {
                            Log.i(TAG, "onMessageReceived isSticky: ");
                            //The below line helps to avoid duplicate rendering when using custom render implementation via CT
                            CleverTapAPI.processPushNotification(getApplicationContext(), extras);
                            ////////////////////////////////////////////////////

                            //method to render your notification
                            showStickyNotification(getApplicationContext(), remoteMessage, extras);
                        } else if (extras.containsKey("isProgressTimerSticky")) {
                            Log.i(TAG, "onMessageReceived isProgressTimer: ");
                            //The below line helps to avoid duplicate rendering when using custom render implementation via CT
                            CleverTapAPI.processPushNotification(getApplicationContext(), extras);
                            ////////////////////////////////////////////////////

                            //method to render your notification
                            //testMethod(extras,remoteMessage);
                            progressBarNotification(getApplicationContext(), remoteMessage, extras);
                        } else {
                            boolean status = new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
                            Log.i(TAG, "onMessageReceived status: " + status);
                        }
                    }
                } else {
                    customRenderNotification(remoteMessage);
                    // not from CleverTap handle yourself or pass to another provider
                }
            }
            //if app is in foreground && banner to be shown //raise event via ct
        } catch (Throwable t) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t);
        }
//        boolean status = new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
//        Log.i("TAG", "onMessageReceived status: "+status);

        if (remoteMessage.getData().size()>0) {
            Map<String,String> data = remoteMessage.getData();
            String title = data.get("nt");
           String message = data.get("nm");
            MyApplication.getInstance().sendNotificationAppInbox(title,message);
            EventBus.getDefault().post(new MessageEvent(title,message));
        }

    }

    private void showStickyNotification(Context context, RemoteMessage remoteMessage, Bundle extras) {
        if (remoteMessage.getData() == null) {
            return;
        }
        String title = remoteMessage.getData().get("nt");
        String message = remoteMessage.getData().get("nm");
        String channelId = remoteMessage.getData().get("wzrk_cid");

        PendingIntent pendingIntent = NotificationActivity.getDismissIntent(RANDOM_NOTIFICATION_ID, context, extras);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle(title)
                .setContentText(message)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setColorized(true)
                .setOngoing(true)
                .addAction(R.drawable.ic_close_icon, "Dismiss", pendingIntent);
//                .setContentIntent(pendingIntent);

        Notification n = builder.build();
        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(RANDOM_NOTIFICATION_ID, n);

        //Method .pushNotificationViewedEvent(extras) to raise push impression event
        MyApplication.getInstance().getClevertapDefaultInstance().pushNotificationViewedEvent(extras);
    }


    private void testMethod( Bundle extras, RemoteMessage remoteMessage){
        String title = remoteMessage.getData().get("nt");
        String message = remoteMessage.getData().get("nm");
        String channelId = remoteMessage.getData().get("wzrk_cid");
        String time = extras.getString("isProgressTimerSticky");
        int timerValue = Integer.parseInt(time);
        /*mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(),"testChannelId1");
        PendingIntent pendingIntent = NotificationActivity.getDismissIntent(id, getApplicationContext(), extras);

        mBuilder.setContentTitle("Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setOngoing(true)
                .addAction(R.drawable.ic_close_icon, "Dismiss", pendingIntent);;
        new ProgressLoader(timerValue,extras).execute();*/
    }

    private void progressBarNotification(Context context, RemoteMessage remoteMessage, Bundle extras) {
        String title = remoteMessage.getData().get("nt");
        String message = remoteMessage.getData().get("nm");
        String channelId = remoteMessage.getData().get("wzrk_cid");
        String time = extras.getString("isProgressTimerSticky");
        int timerValue = Integer.parseInt(time);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = NotificationActivity.getDismissIntent(id, context, extras);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle(title)
                .setContentText(message)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setColorized(true)
                .addAction(R.drawable.ic_close_icon, "Dismiss", pendingIntent);

        new ProgressLoader(manager,builder,timerValue,extras).execute();

        //Method .pushNotificationViewedEvent(extras) to raise push impression event
        MyApplication.getInstance().getClevertapDefaultInstance().pushNotificationViewedEvent(extras);
    }

    //                        new CTFcmMessageHandler().processPushAmp(getApplicationContext(), remoteMessage);

    private void customRenderNotification(RemoteMessage remoteMessage) {
        Log.d(TAG, "customRenderNotification() called with: remoteMessage = [" + remoteMessage + "]");
//// Get the layouts to use in the custom notification
//        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);

// Apply the layouts to the notification

        if (remoteMessage.getData() == null) {
            return;
        }
        String title = remoteMessage.getData().get("nt");
        String message = remoteMessage.getData().get("nm");
        String channelId = remoteMessage.getData().get("wzrk_cid");
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
        Bundle extras = new Bundle();
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            extras.putString(entry.getKey(), entry.getValue());
        }
        PendingIntent pendingIntent = NotificationActivity.getDismissIntent(0,getApplicationContext(), extras);


        NotificationCompat.Builder customNotification = new NotificationCompat.Builder(getApplicationContext(),
                Objects.requireNonNull(channelId))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle(title)
                .setContentText(message)
//                .setColor(getResources().getColor(R.color.gray))
                .setColorized(true)
                .setContentIntent(pendingIntent);
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


    private class ProgressLoader extends AsyncTask<Void, Integer, Integer> {

        int timerValue;
        Bundle extras;
        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;
        Notification notification;

        public ProgressLoader(NotificationManager manager, NotificationCompat.Builder builder,
                              int timer, Bundle extras) {
            this.mBuilder = builder;
            this.mNotifyManager = manager;
            this.timerValue = timer;
            this.extras = extras;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(timerValue, 0, false);
            notification = mBuilder.build();
            notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            mNotifyManager.notify(id, notification);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            mBuilder.setProgress(timerValue, values[0], false);
            notification = mBuilder.build();
            notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            mNotifyManager.notify(id, notification);
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int i;
            for (i = 0; i <= timerValue; i += 1) {
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, timerValue));
                try {
                    // Sleep for 1 second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d("TAG", "sleep failure");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mBuilder.setContentText("Deal Expired");
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            notification = mBuilder.build();
            notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            mNotifyManager.notify(id,notification );
        }
    }
}


