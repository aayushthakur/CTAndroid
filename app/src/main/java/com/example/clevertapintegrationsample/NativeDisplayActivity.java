package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Map;

public class NativeDisplayActivity extends AppCompatActivity implements DisplayUnitListener {

    private String TAG = NativeDisplayActivity.class.getSimpleName();
    private TextView nativeText1,nativeText2;
    private ImageView nativeImage1,customNativeImage1;
    private TextView customNativeText1,customNativeText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_display);
        NativeDisplayActivity.this.setTitle("Native Display");
        MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("Native Display Trigger");
        MyApplication.getInstance().getClevertapDefaultInstance().setDisplayUnitListener(this);
        MyApplication.getInstance().getClevertapDefaultInstance().getAllDisplayUnits();
        nativeText1 = findViewById(R.id.nativeText1);
        nativeText2 = findViewById(R.id.nativeText2);
        nativeImage1 = findViewById(R.id.nativeImage1);
        customNativeText1 = findViewById(R.id.customNativeText1);
        customNativeText2 = findViewById(R.id.customNativeText2);
        customNativeImage1 = findViewById(R.id.customNativeImage1);
    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        Log.d(TAG, "onDisplayUnitsLoaded() called with: units = [" + units.toString() + "]");
        for (CleverTapDisplayUnit cleverTapDisplayUnit : units) {
            ArrayList<CleverTapDisplayUnitContent> contents = cleverTapDisplayUnit.getContents();
            for (CleverTapDisplayUnitContent content : contents) {
                String title = content.getTitle();
                String message = content.getMessage();

                String mediaUrl = content.getMedia();
                /*Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + title + "]");
                Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + message + "]");
                Log.d("TAG", "onDisplayUnitsLoaded() called with: units = [" + mediaUrl + "]");*/
                nativeText1.setText(title);
                nativeText2.setText(message);
                Glide.with(this).load(mediaUrl).into(nativeImage1);
            }
            //CustomKV
            Map<String, String> customMap = cleverTapDisplayUnit.getCustomExtras();
            if (customMap==null){
                return;
            }
            if (customMap.containsKey("native_display_type") &&
                    customMap.get("native_display_type").equals("title_message_image_1")){
                /*for (Map.Entry<String,String> entry : customMap.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }*/
                String customTitle1 = customMap.get("custom_titlte_1");
                String customMessage1 = customMap.get("custom_message_1");
                String customImage1 = customMap.get("custom_image_1");
                customNativeText1.setText(customTitle1);
                customNativeText2.setText(customMessage1);
                Glide.with(this).load(customImage1).into(customNativeImage1);
            }
        }
    }
}