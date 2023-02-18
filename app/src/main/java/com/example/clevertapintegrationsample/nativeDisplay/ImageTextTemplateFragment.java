package com.example.clevertapintegrationsample.nativeDisplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.clevertapintegrationsample.MyApplication;
import com.example.clevertapintegrationsample.R;
import com.example.clevertapintegrationsample.appinbox.AppInboxModel;
import com.example.clevertapintegrationsample.appinbox.AppInboxRecyclerviewAdapter;

import java.util.ArrayList;
import java.util.Map;

public class ImageTextTemplateFragment extends Fragment implements FragmentCommunicator{
    private String TAG = ImageTextTemplateFragment.class.getSimpleName();

    private TextView nativeText1,nativeText2;
    private ImageView nativeImage1,customNativeImage1;
    private TextView customNativeText1,customNativeText2;

    public ImageTextTemplateFragment() {
        // Required empty public constructor
    }

    public static ImageTextTemplateFragment newInstance() {
        return new ImageTextTemplateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.image_text_native_display_fragment, container, false);
        MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("Native Display Trigger");

        nativeText1 = rootView.findViewById(R.id.nativeText1);
        nativeText2 = rootView.findViewById(R.id.nativeText2);
        nativeImage1 = rootView.findViewById(R.id.nativeImage1);
        customNativeText1 = rootView.findViewById(R.id.customNativeText1);
        customNativeText2 = rootView.findViewById(R.id.customNativeText2);
        customNativeImage1 = rootView.findViewById(R.id.customNativeImage1);

        if (getActivity() instanceof NativeDisplayActivity){
            ((NativeDisplayActivity) getActivity()).setFragmentListener(this);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
//        TextView textViewCounter = view.findViewById(R.id.tv_counter);
//        textViewCounter.setText("Fragment No " + (counter+1));
    }

    @Override
    public void loadData(ArrayList<CleverTapDisplayUnit> units){
        Log.d(TAG, "loadData() called with: units = [" + units.toString() + "]");
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
            }else{
                //backend
            }
        }
    }
}
