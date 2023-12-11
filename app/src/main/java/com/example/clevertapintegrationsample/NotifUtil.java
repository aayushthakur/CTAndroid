package com.example.clevertapintegrationsample;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.clevertapintegrationsample.appinbox.AppInboxRecyclerviewAdapter;
import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

import java.util.Map;

public class NotifUtil {

    private static Context mContext;
    private static WindowManager windowManager;
    private static View floatingView;

    public static void floatingNotif(Context context, RemoteMessage message) {
        mContext = context;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                 windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

                int overlay;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    overlay = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    overlay = WindowManager.LayoutParams.TYPE_PHONE;
                }

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        overlay,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT
                );
                params.gravity = Gravity.START | Gravity.BOTTOM;

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                floatingView = inflater.inflate(R.layout.floating_view, null);
                Bundle bundle = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    bundle.putString(entry.getKey(), entry.getValue());
                }

                String title = message.getData().get("nt");
                String msg = message.getData().get("nm");
//                String imageUrl = message.getData().get("wzrk_bp");

                TextView titleView = floatingView.findViewById(R.id.title);
                titleView.setText(title);

                TextView messageView = floatingView.findViewById(R.id.message);
                messageView.setText(msg);

                /*ImageView imageView = floatingView.findViewById(R.id.image);
                if (!TextUtils.isEmpty(imageUrl)) {
                    Glide.with(context).load(imageUrl).into(imageView);
                }*/

                floatingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        windowManager.removeView(floatingView);
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        MyApplication.getInstance().getClevertapDefaultInstance().pushNotificationClickedEvent(bundle);
                    }
                });

              /*  floatingView.findViewById(R.id.btCross).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        windowManager.removeView(floatingView);
                    }
                });*/

                windowManager.addView(floatingView, params);
                MyApplication.getInstance().getClevertapDefaultInstance().pushNotificationViewedEvent(bundle);
            }
        });
    }
    public static void removeWindowManagerView(){
        windowManager.removeView(floatingView);
    }

}

